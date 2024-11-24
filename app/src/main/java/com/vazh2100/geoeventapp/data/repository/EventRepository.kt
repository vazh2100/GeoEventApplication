package com.vazh2100.geoeventapp.data.repository

import com.vazh2100.geoeventapp.data.network.api.MainApi
import com.vazh2100.geoeventapp.data.storages.device.PreferencesStorage
import com.vazh2100.geoeventapp.data.storages.room.dao.EventDao
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Repository for managing event data from various sources, including local storage and a remote API.
 * Handles filtering, caching, and synchronization of event data.
 */
class EventRepository(
    private val eventDao: EventDao,
    private val mainApi: MainApi,
    private val preferenceStorage: PreferencesStorage,
) {
    private val getNowDate: () -> Instant = { Instant.now() }
    private var lastUpdateTime = MutableStateFlow<Instant?>(null)

    init {
        // Collects the last update time from preference storage and updates the state flow.
        CoroutineScope(Dispatchers.IO).launch {
            preferenceStorage.getLastUpdateTimeFlow().collect {
                lastUpdateTime.value = it
            }
        }
    }

    /**
     * Retrieves a list of events filtered by the specified criteria.
     */
    suspend fun getFilteredEvents(
        eventFilter: EventFilter,
        userLatitude: Double? = null,
        userLongitude: Double? = null,
        hasInternet: Boolean,
    ): List<Event> = withContext(Dispatchers.IO) {
        getAllEvents(hasInternet).filter {
            it.matchesFilter(eventFilter, userLatitude, userLongitude)
        }
    }

    /**
     * Fetches all events, either from the local database or the remote API, depending on cache validity.
     */
    private suspend fun getAllEvents(hasInternet: Boolean): List<Event> {
        val isCacheValid = lastUpdateTime.value?.let {
            ChronoUnit.MINUTES.between(it, getNowDate()) < 30
        } == true
        println("isCacheValid: $isCacheValid")
        println("isCacheValid: ${lastUpdateTime.value}")
        return if (isCacheValid || !hasInternet) {
            eventDao.getAllEvents()
        } else {
            refreshEvents()
            eventDao.getAllEvents()
        }
    }

    /**
     * Updates the local cache of events by fetching fresh data from the remote API.
     */
    private suspend fun refreshEvents() {
        delay(5000L) // Simulates a delay for the API request.
        val eventsFromApi = mainApi.getEvents()
        eventDao.deleteAllEvents()
        eventDao.insertEvents(eventsFromApi)
        preferenceStorage.setLastUpdateTime(getNowDate())
    }
}
