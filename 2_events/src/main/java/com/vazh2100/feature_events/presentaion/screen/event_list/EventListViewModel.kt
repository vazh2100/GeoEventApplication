package com.vazh2100.feature_events.presentaion.screen.event_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vazh2100.core.domain.entities.GPoint
import com.vazh2100.core.domain.entities.NetworkStatus
import com.vazh2100.core.domain.usecase.IGetLocationStatusUseCase
import com.vazh2100.core.domain.usecase.IGetNetworkStatusUseCase
import com.vazh2100.feature_events.domain.entities.event.Event
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams
import com.vazh2100.feature_events.domain.entities.event.EventSortType
import com.vazh2100.feature_events.domain.usecase.GetFilteredEventsUseCase
import com.vazh2100.feature_events.domain.usecase.GetSavedFiltersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

/**
 * ViewModel for the Event List screen. Manages the state of the events, filter settings,
 * network status, and location status. Fetches the events based on user filters and settings.
 */
internal class EventListViewModel(
    private val getSavedFiltersUseCase: GetSavedFiltersUseCase,
    private val getFilteredEventsUseCase: GetFilteredEventsUseCase,
    getNetworkStatusUseCase: IGetNetworkStatusUseCase,
    getLocationStatusUseCase: IGetLocationStatusUseCase,
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
    private val _searchParams = MutableStateFlow<EventSearchParams>(EventSearchParams())
    val eventSearchParams: StateFlow<EventSearchParams> get() = _searchParams

    // Flow for network status
    val networkStatus: StateFlow<NetworkStatus> = getNetworkStatusUseCase.networkStatus

    // Flow for location status and current user's coordinates
    val locationStatus = getLocationStatusUseCase.locationStatus
    val userGPoint: StateFlow<GPoint?> = getLocationStatusUseCase.userGPoint

    init {
        viewModelScope.launch {
            loadSavedFilters()
            loadEvents()
            reactOnUserGPChanged()
        }
    }

    /**
     * Loads the saved event filters from the data source.
     */
    private suspend fun loadSavedFilters() {
        _isLoading.value = true
        val result = getSavedFiltersUseCase.get()
        result.onSuccess {
            _searchParams.value = it
        }.onFailure {
            _errorMessage.value = it.message
            it.printStackTrace()
        }
        _isLoading.value = false
    }

    /**
     * Applies the new event filter and reloads the events if the filter has changed.
     * @param eventSearchParams The new filter to apply.
     */
    fun applyFilters(
        eventSearchParams: EventSearchParams
    ) {
        val changed = eventSearchParams != _searchParams.value
        _searchParams.value = eventSearchParams
        if (changed) {
            viewModelScope.launch {
                loadEvents()
            }
        }
    }

    /**
     * Loads the events based on the current filter.
     */
    private suspend fun loadEvents() {
        _isLoading.value = true
        val result = getFilteredEventsUseCase.get(_searchParams.value)

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

    private fun reactOnUserGPChanged() =
        userGPoint.scan(userGPoint.value) { old, new ->
            val isFromNull = old == null && new != null
            val hasGeoFilter =
                _searchParams.value.radius != null || _searchParams.value.sortType == EventSortType.DISTANCE
            if (isFromNull && hasGeoFilter) {
                loadEvents()
            }
            new
        }.launchIn(viewModelScope)
}
