package com.vazh2100.feature_events.data.repository

import com.vazh2100.feature_events.data.network.api.EventsApi
import com.vazh2100.feature_events.data.storages.device.EventsPreferencesStorage
import com.vazh2100.feature_events.data.storages.room.dao.EventDao
import com.vazh2100.feature_events.domain.entities.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Repository for managing event data from various sources, including local storage and a remote API.
 * Handles filtering, caching, and synchronization of event data.
 */
internal class EventRepository(
    private val eventDao: EventDao,
    private val eventsApi: EventsApi,
    private val preferenceStorage: EventsPreferencesStorage,
) {

    private val getNowDate: () -> Instant = { Instant.now() }
    private var lastUpdateTime = MutableStateFlow<Instant?>(null)
    private var events: List<Event> = listOf()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            eventDao.getAllEvents().let {
                events = it
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            preferenceStorage.getLastUpdateTimeFlow().collect {
                lastUpdateTime.value = it
            }
        }
    }

    /**
     * Fetches all events, either from the local database or the remote API, depending on cache validity.
     */
    suspend fun getAllEvents(hasInternet: Boolean): List<Event> {
        val isCacheValid = lastUpdateTime.value?.let {
            ChronoUnit.MINUTES.between(it, getNowDate()) < CACHE_VALIDITY_TIME
        } == true
        println("isCacheValid: $isCacheValid")
        println("lastUpdateTime: ${lastUpdateTime.value}")
        return if (isCacheValid || !hasInternet) {
            events
        } else {
            refreshEvents()
            events
        }
    }

    /**
     * Updates the local cache of events by fetching fresh data from the remote API.
     */
    private suspend fun refreshEvents() {
        delay(DELAY)
        val eventsFromApi = eventsApi.getEvents()
        events = eventsFromApi
        eventDao.deleteAllEvents()
        eventDao.insertEvents(eventsFromApi)
        preferenceStorage.setLastUpdateTime(getNowDate())
    }
    private companion object {
        const val DELAY = 5000L
        const val CACHE_VALIDITY_TIME = 30 // seconds
    }
}
