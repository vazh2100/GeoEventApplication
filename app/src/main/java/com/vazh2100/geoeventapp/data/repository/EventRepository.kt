package com.vazh2100.geoeventapp.data.repository

import com.vazh2100.geoeventapp.data.api.MainApi
import com.vazh2100.geoeventapp.data.storages.room.dao.EventDao
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.temporal.ChronoUnit

class EventRepository(
    private val eventDao: EventDao,
    private val mainApi: MainApi,

    ) {
    private val lastUpdateTimeProvider: () -> Instant = { Instant.now() }
    private var lastUpdateTime: Instant? = null

    suspend fun getFilteredEvents(
        eventFilter: EventFilter,
        userLatitude: Double? = null,
        userLongitude: Double? = null,
    ): List<Event> = withContext(Dispatchers.IO) {
        getAllEvents().filter {
            it.matchesFilter(eventFilter, userLatitude, userLongitude)
        }
    }


    private suspend fun getAllEvents(): List<Event> {
        val isCacheValid = lastUpdateTime?.let {
            ChronoUnit.MINUTES.between(it, lastUpdateTimeProvider()) < 30
        } == true

        return if (isCacheValid) {
            eventDao.getAllEvents()
        } else {
            refreshEvents()
            eventDao.getAllEvents()
        }
    }

    private suspend fun refreshEvents() {
        val eventsFromApi = mainApi.getEvents()
        eventDao.deleteAllEvents()
        eventDao.insertEvents(eventsFromApi)
        lastUpdateTime = lastUpdateTimeProvider()
    }
}
