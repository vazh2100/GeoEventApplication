package com.vazh2100.geoeventapp.data.storages.device

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.Instant

/**
 * Handles persistent storage of user preferences, including filters
 */
class PreferencesStorage(private val context: Context) {

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
     * @param eventFilter The event filter containing multiple criteria.
     */
    suspend fun saveEventFilter(eventFilter: EventFilter) {
        if (lastFilter == eventFilter) return
        lastFilter = eventFilter
        with(eventFilter) {
            setEventType(type)
            setStartDate(startDate)
            setEndDate(endDate)
            setRadius(radius)
        }
    }

    /**
     * Retrieves the entire event filter from persistent storage.
     * @return An [EventFilter] object with the stored criteria or defaults.
     */
    suspend fun getEventFilter(): EventFilter {
        val type = getEventTypeFlow().firstOrNull()?.let { EventType.valueOf(it) }
        val startDate = getStartDateFlow().firstOrNull()
        val endDate = getEndDateFlow().firstOrNull()
        val radius = getRadiusFlow().firstOrNull()

        return EventFilter(type, startDate, endDate, radius).also { lastFilter = it }
    }

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_preferences")
        private val EVENT_TYPE_KEY = stringPreferencesKey("event_type")
        private val START_DATE_KEY = longPreferencesKey("start_date")
        private val END_DATE_KEY = longPreferencesKey("end_date")
        private val RADIUS_KEY = intPreferencesKey("radius")
        private val LAST_UPDATE_TIME_KEY = longPreferencesKey("last_update_time")
    }

    private var lastFilter: EventFilter? = null


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


}
