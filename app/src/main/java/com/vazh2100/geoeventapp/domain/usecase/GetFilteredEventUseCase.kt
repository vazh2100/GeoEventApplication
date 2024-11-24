package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.data.repository.LocationRepository
import com.vazh2100.geoeventapp.data.storages.device.PreferencesStorage
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter

class GetFilteredEventsUseCase(
    private val eventRepository: EventRepository,
    private val locationRepository: LocationRepository,
    private val preferencesStorage: PreferencesStorage
) {
    suspend fun execute(
        eventFilter: EventFilter
    ): Result<List<Event>> {

        try {
            preferencesStorage.saveEventFilter(eventFilter)
        } catch (_: Exception) {
            return Result.failure(Exception("Failed to save event filter"))
        }

        val filteredEvents = try {
            val (latitude, longitude) = Pair(47.2400899, 39.8140522)
            eventRepository.getFilteredEvents(
                eventFilter = eventFilter, userLatitude = latitude, userLongitude = longitude
            )
        } catch (_: Exception) {
            return Result.failure(Exception("Failed to fetch filtered events"))
        }
        return Result.success(filteredEvents)

    }

}


