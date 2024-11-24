package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.data.storages.device.PreferencesStorage
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus

/**
 * Use case for retrieving a filtered list of events.
 * This class handles filtering logic by combining user preferences,
 * network status, and location data.
 */
class GetFilteredEventsUseCase(
    private val eventRepository: EventRepository,
    private val preferencesStorage: PreferencesStorage,
    private val getNetworkStatusUseCase: GetNetworkStatusUseCase,
    private val getLocationStatusUseCase: GetLocationStatusUseCase
) {

    /**
     * Retrieves a list of events filtered based on user preferences and current conditions.
     */
    suspend fun get(
        eventFilter: EventFilter
    ): Result<List<Event>> {
        // Save the filter preferences locally
        try {
            preferencesStorage.saveEventFilter(eventFilter)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Check the current network status
        val hasInternet = getNetworkStatusUseCase.networkStatus.value == NetworkStatus.CONNECTED
        // Get the user's current coordinates
        val currentCoordinates = getLocationStatusUseCase.currentCoordinates.value

        val filteredEvents: List<Event>
        try {
            // Fetch the filtered events from the repository
            filteredEvents = eventRepository.getFilteredEvents(
                eventFilter = eventFilter,
                userLatitude = currentCoordinates?.first,
                userLongitude = currentCoordinates?.second,
                hasInternet = hasInternet
            )
        } catch (_: Exception) {
            // Return a failure result if fetching events fails
            return Result.failure(Exception("Failed to get events"))
        }

        // Return a successful result with the filtered events
        return Result.success(filteredEvents)
    }
}
