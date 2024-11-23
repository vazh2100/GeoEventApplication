package com.vazh2100.geoeventapp.data.repository

import com.vazh2100.geoeventapp.data.api.MainApi
import com.vazh2100.geoeventapp.data.storages.room.dao.EventDao
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class EventRepository(
    private val eventDao: EventDao,
    private val mainApi: MainApi,
    private val lastUpdateTimeProvider: () -> LocalDateTime = { LocalDateTime.now() }
) {
    private var lastUpdateTime: LocalDateTime? = null

    suspend fun getFilteredEvents(
        type: EventType? = null,
        startDate: ZonedDateTime? = null,
        endDate: ZonedDateTime? = null,
        userLatitude: Double? = null,
        userLongitude: Double? = null,
        radius: Double? = null
    ): List<Event> = withContext(Dispatchers.IO) {
        val events = getAllEvents()
        events.filter {
            (type == null || it.type == type) && (startDate == null || it.date.isAfter(
                startDate.toInstant()
            )) && (endDate == null || it.date.isBefore(
                endDate.toInstant()
            )) && (radius == null || userLatitude == null || userLongitude == null || haversine(
                it.latitude, it.longitude, userLatitude, userLongitude
            ) <= radius)
        }
    }

    private suspend fun getAllEvents(): List<Event> {
        val isCacheValid = lastUpdateTime?.let {
            ChronoUnit.MINUTES.between(it, lastUpdateTimeProvider()) < 30
        } == true

        return if (isCacheValid) {
            eventDao.getAllEvents()
        } else {
            refreshEvents() // Обновляем данные из API
            eventDao.getAllEvents()
        }
    }

    suspend fun refreshEvents() {
        val eventsFromApi = mainApi.getEvents()
        eventDao.deleteAllEvents()
        eventDao.insertEvents(eventsFromApi)
        lastUpdateTime = lastUpdateTimeProvider()
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(
            Math.toRadians(lat2)
        ) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
}
