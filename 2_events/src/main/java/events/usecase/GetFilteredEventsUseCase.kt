package events.usecase

import android.util.Log
import events.data.repository.EventRepository
import events.data.storage.EventsPreferencesStorage
import events.entities.Event
import events.entities.EventSearchParams
import events.entities.EventsProcessor.searchWith
import geolocation.usecase.IGetLocationStatusUseCase
import kotlinx.coroutines.flow.first
import network.entity.NetworkStatus.CONNECTED
import network.usecase.IObserveNetworkStateUseCase

/** Retrieves a list of events filtered based on user preferences and current conditions. */
internal class GetFilteredEventsUseCase(
    private val eventRepository: EventRepository,
    private val eventsPreferencesStorage: EventsPreferencesStorage,
    private val getNetworkStatus: IObserveNetworkStateUseCase,
    private val getLocationStatus: IGetLocationStatusUseCase,
) {

    suspend operator fun invoke(eventSearchParams: EventSearchParams): Result<List<Event>> {
        // Save the filter preferences locally
        try {
            eventsPreferencesStorage.saveEventSearchParams(eventSearchParams)
        } catch (e: Exception) {
            Log.e("", "", e)
        }
        // Check the current network status
        val hasInternet = getNetworkStatus().first() == CONNECTED
        // Get the user's current coordinates
        val userGPoint = getLocationStatus.userGPoint.value
        val events: List<Event>
        try {
            // Fetch the events from the repository
            events = eventRepository.getAllEvents(hasInternet)
        } catch (_: Exception) {
            return Result.failure(Exception("Failed to get events"))
        }
        // Filter and sort events
        val filteredEvents = events.searchWith(eventSearchParams.apply { gPoint = userGPoint })
        // Return a successful result with the filtered events
        return Result.success(filteredEvents)
    }
}
