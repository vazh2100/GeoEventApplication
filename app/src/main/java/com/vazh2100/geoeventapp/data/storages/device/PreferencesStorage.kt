package com.vazh2100.geoeventapp.data.storages.device

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class PreferencesStorage(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_preferences")

        private val EVENT_TYPE_KEY = stringPreferencesKey("event_type")
        private val START_DATE_KEY = longPreferencesKey("start_date")
        private val END_DATE_KEY = longPreferencesKey("end_date")
        private val RADIUS_KEY = doublePreferencesKey("radius")

        private val dateFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
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

    suspend fun setStartDate(startDate: ZonedDateTime?) {
        context.dataStore.edit { preferences ->
            if (startDate != null) {
                preferences[START_DATE_KEY] = startDate.toEpochSecond()
            } else {
                preferences.remove(START_DATE_KEY)
            }
        }
    }

    fun getStartDateFlow(): Flow<ZonedDateTime?> {
        return context.dataStore.data.map { preferences ->
            preferences[START_DATE_KEY]?.let { epochSeconds ->
                ZonedDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(epochSeconds),
                    java.time.ZoneId.systemDefault()
                )
            }
        }
    }

    suspend fun setEndDate(endDate: ZonedDateTime?) {
        context.dataStore.edit { preferences ->
            if (endDate != null) {
                preferences[END_DATE_KEY] = endDate.toEpochSecond()
            } else {
                preferences.remove(END_DATE_KEY)
            }
        }
    }

    fun getEndDateFlow(): Flow<ZonedDateTime?> {
        return context.dataStore.data.map { preferences ->
            preferences[END_DATE_KEY]?.let { epochSeconds ->
                ZonedDateTime.ofInstant(
                    java.time.Instant.ofEpochSecond(epochSeconds),
                    java.time.ZoneId.systemDefault()
                )
            }
        }
    }

    suspend fun setRadius(radius: Double?) {
        context.dataStore.edit { preferences ->
            if (radius != null) {
                preferences[RADIUS_KEY] = radius
            } else {
                preferences.remove(RADIUS_KEY)
            }
        }
    }

    fun getRadiusFlow(): Flow<Double?> {
        return context.dataStore.data.map { preferences ->
            preferences[RADIUS_KEY]
        }
    }
}
