package com.vazh2100.geoeventapp.presentaion.screen.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus
import com.vazh2100.geoeventapp.domain.usecase.GetFilteredEventsUseCase
import com.vazh2100.geoeventapp.domain.usecase.GetLocationStatusUseCase
import com.vazh2100.geoeventapp.domain.usecase.GetNetworkStatusUseCase
import com.vazh2100.geoeventapp.domain.usecase.GetSavedFiltersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * ViewModel for the Event List screen. Manages the state of the events, filter settings,
 * network status, and location status. Fetches the events based on user filters and settings.
 */
class EventListViewModel(
    private val getSavedFiltersUseCase: GetSavedFiltersUseCase,
    private val getFilteredEventsUseCase: GetFilteredEventsUseCase,
    private val getNetworkStatusUseCase: GetNetworkStatusUseCase,
    private val getLocationStatusUseCase: GetLocationStatusUseCase,
) : ViewModel() {

    // State to hold the list of events to be displayed
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    // State to track if data is currently loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // State to hold any error message that occurs during data loading
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // State to store the currently applied event filter
    private val _filter = MutableStateFlow<EventFilter>(EventFilter())
    val filter: StateFlow<EventFilter> get() = _filter

    // Flow for network status
    val networkStatus: StateFlow<NetworkStatus> = getNetworkStatusUseCase.networkStatus

    // Flow for location status and current user's coordinates
    val locationStatus = getLocationStatusUseCase.locationStatus
    val geoPosition = getLocationStatusUseCase.currentCoordinates

    init {
        loadSavedFilters()
    }

    /**
     * Loads the saved event filters from the data source.
     */
    private fun loadSavedFilters() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getSavedFiltersUseCase.get()

            result.onSuccess { eventFilter ->
                // Set the loaded filter and fetch the events based on it
                _filter.value = eventFilter
                loadEvents()
            }.onFailure { error ->
                _errorMessage.value = error.message
                error.printStackTrace()
            }
            _isLoading.value = false
        }
    }

    /**
     * Applies the new event filter and reloads the events if the filter has changed.
     * @param eventFilter The new filter to apply.
     */
    fun applyFilters(
        eventFilter: EventFilter
    ) {
        val changed = eventFilter != _filter.value
        _filter.value = eventFilter
        if (changed) {
            // Reload events when the filter changes
            loadEvents()
        }
    }

    /**
     * Loads the events based on the current filter.
     */
    private fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getFilteredEventsUseCase.get(_filter.value)

            result.onSuccess { events ->
                // Update the events state with the new list of events
                _events.value = events
                _errorMessage.value = null
            }.onFailure { error ->
                // Handle any errors that occur while loading events
                _errorMessage.value = error.message
                error.printStackTrace()
            }

            _isLoading.value = false
        }
    }
}

