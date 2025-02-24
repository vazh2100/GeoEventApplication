package com.vazh2100.feature_events.domain.usecase

import android.util.Log
import com.vazh2100.core.domain.usecase.IGetLocationStatusUseCase
import com.vazh2100.feature_events.data.repository.EventRepository
import com.vazh2100.feature_events.data.storages.device.EventsPreferencesStorage
import com.vazh2100.feature_events.domain.entities.event.Event
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams
import com.vazh2100.feature_events.domain.entities.event.EventsProcessor.searchWith
import com.vazh2100.network.entity.NetworkStatus
import com.vazh2100.network.usecase.IGetNetworkStatusUseCase

/**
 * Use case for retrieving a filtered list of events.
 * This class handles filtering logic by combining user preferences,
 * network status, and location data.
 */
internal class GetFilteredEventsUseCase(
    private val eventRepository: EventRepository,
    private val eventsPreferencesStorage: EventsPreferencesStorage,
    private val getNetworkStatusUseCase: IGetNetworkStatusUseCase,
    private val getLocationStatusUseCase: IGetLocationStatusUseCase,
) {

    /**
     * Retrieves a list of events filtered based on user preferences and current conditions.
     */
    suspend fun get(
        eventSearchParams: EventSearchParams
    ): Result<List<Event>> {
        // Save the filter preferences locally
        try {
            eventsPreferencesStorage.saveEventSearchParams(eventSearchParams)
        } catch (e: Exception) {
            Log.e("", "", e)
        }
        // Check the current network status
        val hasInternet = getNetworkStatusUseCase.networkStatus.value == NetworkStatus.CONNECTED
        // Get the user's current coordinates
        val userGPoint = getLocationStatusUseCase.userGPoint.value
        val events: List<Event>
        try {
            // Fetch the events from the repository
            events = eventRepository.getAllEvents(hasInternet)
        } catch (_: Exception) {
            // Return a failure result if fetching events fails
            return Result.failure(Exception("Failed to get events"))
        }
        // Filter and sort events
        val filteredEvents = events.searchWith(
            eventSearchParams,
            userGPoint
        )
        // Return a successful result with the filtered events
        return Result.success(filteredEvents)
    }
}
