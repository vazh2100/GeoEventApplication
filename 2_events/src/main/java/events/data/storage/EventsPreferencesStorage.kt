package events.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import events.entities.EventSearchParams
import events.entities.EventSortType
import events.entities.EventType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant

/**
 * Handles persistent storage of user preferences, including filters and update times.
 */
internal class EventsPreferencesStorage(private val context: Context) {

    /**
     * Saves the entire event filter to persistent storage.
     * @param eventSearchParams The event filter containing multiple criteria.
     */
    suspend fun saveEventSearchParams(eventSearchParams: EventSearchParams) = withContext(Dispatchers.IO) {
        context.dataStore.edit { preferences ->
            eventSearchParams.type?.let { preferences[EVENT_TYPE_KEY] = it.name }
                ?: preferences.remove(EVENT_TYPE_KEY)

            eventSearchParams.startDate?.let { preferences[START_DATE_KEY] = it.toEpochMilli() }
                ?: preferences.remove(START_DATE_KEY)

            eventSearchParams.endDate?.let { preferences[END_DATE_KEY] = it.toEpochMilli() }
                ?: preferences.remove(END_DATE_KEY)

            eventSearchParams.radius?.let { preferences[RADIUS_KEY] = it }
                ?: preferences.remove(RADIUS_KEY)

            eventSearchParams.sortType?.let { preferences[EVENT_SORT_TYPE_KEY] = it.name }
                ?: preferences.remove(EVENT_SORT_TYPE_KEY)
        }
        Unit
    }

    /**
     * Retrieves the entire event filter from persistent storage.
     * @return An [EventSearchParams] object with the stored criteria or defaults.
     */
    suspend fun getEventSearchParams(): EventSearchParams = withContext(Dispatchers.IO) {
        val preferences = context.dataStore.data.first()
        val type = preferences[EVENT_TYPE_KEY]?.let { EventType.valueOf(it) }
        val startDate = preferences[START_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        val endDate = preferences[END_DATE_KEY]?.let { Instant.ofEpochMilli(it) }
        val radius = preferences[RADIUS_KEY]
        val sortType = preferences[EVENT_SORT_TYPE_KEY]?.let { EventSortType.valueOf(it) }
        EventSearchParams(type, startDate, endDate, radius, null, sortType)
    }

    /**
     * Updates the timestamp of the last successful update of Event
     */
    suspend fun setLastUpdateTime(lastUpdateTime: Instant) = withContext(Dispatchers.IO) {
        context.dataStore.edit { preferences -> preferences[LAST_UPDATE_TIME_KEY] = lastUpdateTime.toEpochMilli() }
        Unit
    }

    /**Retrieves the timestamp of the last successful update*/
    suspend fun lastUpdateTime(): Instant? = withContext(Dispatchers.IO) {
        context.dataStore.data.map { preferences ->
            preferences[LAST_UPDATE_TIME_KEY]?.let { Instant.ofEpochMilli(it) }
        }.first()
    }

    private companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "filter_preferences")
        val EVENT_TYPE_KEY = stringPreferencesKey("event_type")
        val START_DATE_KEY = longPreferencesKey("start_date")
        val END_DATE_KEY = longPreferencesKey("end_date")
        val RADIUS_KEY = intPreferencesKey("radius")
        val EVENT_SORT_TYPE_KEY = stringPreferencesKey("sort_type")
        val LAST_UPDATE_TIME_KEY = longPreferencesKey("last_update_time")
    }
}
