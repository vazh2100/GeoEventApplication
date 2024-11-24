package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.data.storages.device.PreferencesStorage
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus

class GetFilteredEventsUseCase(
    private val eventRepository: EventRepository,
    private val preferencesStorage: PreferencesStorage,
    private val getNetworkStatusUseCase: GetNetworkStatusUseCase,
    private val getLocationStatusUseCase: GetLocationStatusUseCase
) {

    suspend fun get(
        eventFilter: EventFilter
    ): Result<List<Event>> {

        // Сохранение фильтра в настройки
        try {
            preferencesStorage.saveEventFilter(eventFilter)
        } catch (_: Exception) {
//        return     Result.failure(Exception("Failed to save event filter"))
        }

        val hasInternet = getNetworkStatusUseCase.networkStatus.value == NetworkStatus.CONNECTED
        val currentCoordinates = getLocationStatusUseCase.currentCoordinates.value

        val filteredEvents: List<Event>
        try {
            filteredEvents = eventRepository.getFilteredEvents(
                eventFilter = eventFilter,
                userLatitude = currentCoordinates?.first,
                userLongitude = currentCoordinates?.second,
                hasInternet = hasInternet
            )

        } catch (_: Exception) {
            return Result.failure(Exception("Failed to get filtered events"))
        }
        return Result.success(filteredEvents)
    }
}



