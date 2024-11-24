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

class EventRepository(
    private val eventDao: EventDao,
    private val mainApi: MainApi,
    private val preferenceStorage: PreferencesStorage,
) {
    private val getNowDate: () -> Instant = { Instant.now() }
    private var lastUpdateTime = MutableStateFlow<Instant?>(null)


    init {
        preferenceStorage.getLastUpdateTimeFlow()
        CoroutineScope(Dispatchers.IO).launch {
            preferenceStorage.getLastUpdateTimeFlow().collect {
                lastUpdateTime.value = it
            }
        }
    }

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

    private suspend fun refreshEvents() {
        delay(5000L)
        val eventsFromApi = mainApi.getEvents()
        eventDao.deleteAllEvents()
        eventDao.insertEvents(eventsFromApi)
        preferenceStorage.setLastUpdateTime(getNowDate())
    }
}
