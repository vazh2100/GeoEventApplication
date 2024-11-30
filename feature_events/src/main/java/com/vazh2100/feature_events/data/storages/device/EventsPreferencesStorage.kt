package com.vazh2100.feature_events.data.storages.device

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams
import com.vazh2100.feature_events.domain.entities.event.EventSortType
import com.vazh2100.feature_events.domain.entities.event.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Instant

/**
 * Handles persistent storage of user preferences, including filters and update times.
 */
internal class EventsPreferencesStorage(private val context: Context) {

    /**
     * Saves the entire event filter to persistent storage.
     * @param eventSearchParams The event filter containing multiple criteria.
     */
    suspend fun saveEventSearchParams(eventSearchParams: EventSearchParams) {
        lastSearch = eventSearchParams
        context.dataStore.edit { preferences ->
            if (eventSearchParams.type != null) {
                preferences[EVENT_TYPE_KEY] = eventSearchParams.type.name
            } else {
                preferences.remove(EVENT_TYPE_KEY)
            }

            if (eventSearchParams.startDate != null) {
                preferences[START_DATE_KEY] = eventSearchParams.startDate.toEpochMilli()
            } else {
                preferences.remove(START_DATE_KEY)
            }

            if (eventSearchParams.endDate != null) {
                preferences[END_DATE_KEY] = eventSearchParams.endDate.toEpochMilli()
            } else {
                preferences.remove(END_DATE_KEY)
            }

            if (eventSearchParams.radius != null) {
                preferences[RADIUS_KEY] = eventSearchParams.radius
            } else {
                preferences.remove(RADIUS_KEY)
            }

            if (eventSearchParams.sortType != null) {
                preferences[EVENT_SORT_TYPE_KEY] = eventSearchParams.sortType.name
            } else {
                preferences.remove(EVENT_SORT_TYPE_KEY)
            }
        }
    }

    /**
     * Retrieves the entire event filter from persistent storage.
     * @return An [EventSearchParams] object with the stored criteria or defaults.
     */
    suspend fun getEventSearchParams(): EventSearchParams {
        val preferences = context.dataStore.data.first()
        val type = preferences[EVENT_TYPE_KEY]?.let { EventType.valueOf(it) }
        val startDate = preferences[START_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        val endDate = preferences[END_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        val radius = preferences[RADIUS_KEY]
        val sortType = preferences[EVENT_SORT_TYPE_KEY]?.let { EventSortType.valueOf(it) }
        return EventSearchParams(type, startDate, endDate, radius, sortType).also {
            lastSearch = it
        }
    }

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
}
