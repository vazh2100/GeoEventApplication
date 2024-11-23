package com.vazh2100.geoeventapp.data.storages.device

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class PreferencesStorage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_preferences")
        private val EVENT_TYPE_KEY = stringPreferencesKey("event_type")
        private val START_DATE_KEY = longPreferencesKey("start_date")
        private val END_DATE_KEY = longPreferencesKey("end_date")
        private val RADIUS_KEY = intPreferencesKey("radius")
    }


    suspend fun setEventType(eventType: String?) {
        context.dataStore.edit { preferences ->
            if (eventType != null) {
                preferences[EVENT_TYPE_KEY] = eventType
            } else {
                preferences.remove(EVENT_TYPE_KEY)
            }
        }
    }

    fun getEventTypeFlow(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[EVENT_TYPE_KEY]
        }
    }

    suspend fun setStartDate(startDate: Instant?) {
        context.dataStore.edit { preferences ->
            if (startDate != null) {
                preferences[START_DATE_KEY] = startDate.toEpochMilli()
            } else {
                preferences.remove(START_DATE_KEY)
            }
        }
    }

    fun getStartDateFlow(): Flow<Instant?> {
        return context.dataStore.data.map { preferences ->
            preferences[START_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        }
    }

    suspend fun setEndDate(endDate: Instant?) {
        context.dataStore.edit { preferences ->
            if (endDate != null) {
                preferences[END_DATE_KEY] = endDate.toEpochMilli()
            } else {
                preferences.remove(END_DATE_KEY)
            }
        }
    }

    fun getEndDateFlow(): Flow<Instant?> {
        return context.dataStore.data.map { preferences ->
            preferences[END_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        }
    }

    suspend fun setRadius(radius: Int?) {
        context.dataStore.edit { preferences ->
            if (radius != null) {
                preferences[RADIUS_KEY] = radius
            } else {
                preferences.remove(RADIUS_KEY)
            }
        }
    }

    fun getRadiusFlow(): Flow<Int?> {
        return context.dataStore.data.map { preferences ->
            preferences[RADIUS_KEY]
        }
    }
}
