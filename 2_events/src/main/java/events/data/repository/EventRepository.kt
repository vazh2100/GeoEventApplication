package events.data.repository

import events.data.api.EventsApi
import events.data.storage.EventsPreferencesStorage
import events.data.storage.room.EventDao
import events.entities.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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
    private companion object {
        const val DELAY = 5000L
        const val CACHE_VALIDITY_TIME = 30 // seconds
    }

    private val getNowDate = { Instant.now() }

    /** Fetches all events, either from the local database or the remote API, depending on cache validity. */
    suspend fun getAllEvents(hasInternet: Boolean): List<Event> = withContext(Dispatchers.IO) {
        val lastUpdateTime = preferenceStorage.lastUpdateTime()
        val isCacheValid = lastUpdateTime?.let {
            ChronoUnit.MINUTES.between(it, getNowDate()) < CACHE_VALIDITY_TIME
        } == true
        println("isCacheValid: $isCacheValid")
        println("lastUpdateTime: $lastUpdateTime")
        if (isCacheValid || !hasInternet) eventDao.getAllEvents() else refreshedEvents()
    }

    /** Updates the local cache of events by fetching fresh data from the remote API.*/
    private suspend fun refreshedEvents() = withContext(Dispatchers.IO) {
        delay(DELAY)
        val eventsFromApi = eventsApi.getEvents()
        eventDao.deleteAllEvents()
        eventDao.insertEvents(eventsFromApi)
        preferenceStorage.setLastUpdateTime(getNowDate())
        eventsFromApi
    }
}
