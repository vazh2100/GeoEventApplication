package com.vazh2100.feature_events.data.storages.device

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams
import com.vazh2100.feature_events.domain.entities.event.EventSortType
import com.vazh2100.feature_events.domain.entities.event.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.Instant

/**
 * Handles persistent storage of user preferences, including filters
 */
internal class EventsPreferencesStorage(private val context: Context) {

    /**
     * Updates the timestamp of the last successful update of Event
     */
    suspend fun setLastUpdateTime(lastUpdateTime: Instant) {
        context.dataStore.edit { preferences ->
            preferences[LAST_UPDATE_TIME_KEY] = lastUpdateTime.toEpochMilli()
        }
    }

    /**
     * Retrieves the timestamp of the last successful update as a Flow.
     */
    fun getLastUpdateTimeFlow(): Flow<Instant?> {
        return context.dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_TIME_KEY]?.let { Instant.ofEpochMilli(it) }
        }
    }

    /**
     * Saves the entire event filter to persistent storage.
     * @param eventSearchParams The event filter containing multiple criteria.
     */
    suspend fun saveEventSearchParams(eventSearchParams: EventSearchParams) {
        if (lastSearch == eventSearchParams) return
        lastSearch = eventSearchParams
        with(eventSearchParams) {
            setEventType(type)
            setStartDate(startDate)
            setEndDate(endDate)
            setRadius(radius)
            setSortType(sortType)
        }
    }

    /**
     * Retrieves the entire event filter from persistent storage.
     * @return An [EventSearchParams] object with the stored criteria or defaults.
     */
    suspend fun getEventSearchParams(): EventSearchParams {
        val type = getEventTypeFlow().firstOrNull()?.let { EventType.valueOf(it) }
        val startDate = getStartDateFlow().firstOrNull()
        val endDate = getEndDateFlow().firstOrNull()
        val radius = getRadiusFlow().firstOrNull()
        val sortType = getSortType().firstOrNull()?.let { EventSortType.valueOf(it) }


        return EventSearchParams(type, startDate, endDate, radius, sortType).also {
            lastSearch = it
        }
    }

    companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_preferences")
        private val EVENT_TYPE_KEY = stringPreferencesKey("event_type")
        private val START_DATE_KEY = longPreferencesKey("start_date")
        private val END_DATE_KEY = longPreferencesKey("end_date")
        private val RADIUS_KEY = intPreferencesKey("radius")
        private val EVENT_SORT_TYPE_KEY = stringPreferencesKey("sort_type")
        private val LAST_UPDATE_TIME_KEY = longPreferencesKey("last_update_time")
    }

    private var lastSearch: EventSearchParams? = null

    private suspend fun setEventType(eventType: EventType?) {
        context.dataStore.edit { preferences ->
            if (eventType != null) {
                preferences[EVENT_TYPE_KEY] = eventType.name
            } else {
                preferences.remove(EVENT_TYPE_KEY)
            }
        }
    }

    private fun getEventTypeFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[EVENT_TYPE_KEY]
        }
    }

    private suspend fun setStartDate(startDate: Instant?) {
        context.dataStore.edit { preferences ->
            if (startDate != null) {
                preferences[START_DATE_KEY] = startDate.toEpochMilli()
            } else {
                preferences.remove(START_DATE_KEY)
            }
        }
    }

    private fun getStartDateFlow(): Flow<Instant?> {
        return context.dataStore.data.map { preferences ->
            preferences[START_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        }
    }

    private suspend fun setEndDate(endDate: Instant?) {
        context.dataStore.edit { preferences ->
            if (endDate != null) {
                preferences[END_DATE_KEY] = endDate.toEpochMilli()
            } else {
                preferences.remove(END_DATE_KEY)
            }
        }
    }

    private fun getEndDateFlow(): Flow<Instant?> {
        return context.dataStore.data.map { preferences ->
            preferences[END_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        }
    }

    private suspend fun setRadius(radius: Int?) {
        context.dataStore.edit { preferences ->
            if (radius != null) {
                preferences[RADIUS_KEY] = radius
            } else {
                preferences.remove(RADIUS_KEY)
            }
        }
    }

    private fun getRadiusFlow(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[RADIUS_KEY]
        }
    }

    private suspend fun setSortType(eventSortType: EventSortType?) {
        context.dataStore.edit { preferences ->
            if (eventSortType != null) {
                preferences[EVENT_SORT_TYPE_KEY] = eventSortType.name
            } else {
                preferences.remove(EVENT_SORT_TYPE_KEY)
            }
        }
    }

    private fun getSortType(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[EVENT_SORT_TYPE_KEY]
        }
    }
}
