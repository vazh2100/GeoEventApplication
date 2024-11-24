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

class EventListViewModel(
    private val getSavedFiltersUseCase: GetSavedFiltersUseCase,
    private val getFilteredEventsUseCase: GetFilteredEventsUseCase,
    private val getNetworkStatusUseCase: GetNetworkStatusUseCase,
    private val getLocationStatusUseCase: GetLocationStatusUseCase,
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage


    private val _filter = MutableStateFlow<EventFilter>(EventFilter())
    val filter: StateFlow<EventFilter> get() = _filter

    val networkStatus: StateFlow<NetworkStatus> = getNetworkStatusUseCase.networkStatus

    val locationStatus = getLocationStatusUseCase.locationStatus
    val geoPosition = getLocationStatusUseCase.currentCoordinates

    init {
        loadSavedFilters()
    }


    private fun loadSavedFilters() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getSavedFiltersUseCase.execute()

            result.onSuccess { eventFilter ->
                _filter.value = eventFilter
                loadEvents()
            }.onFailure { error ->
                _errorMessage.value = error.message
                error.printStackTrace()
            }
            _isLoading.value = false
        }
    }

    fun applyFilters(
        eventFilter: EventFilter
    ) {
        val changed = eventFilter != _filter.value
        _filter.value = eventFilter
        if (changed) {
            loadEvents()
        }

    }

    private fun loadEvents(eventFilter: EventFilter = _filter.value) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getFilteredEventsUseCase.get(eventFilter)

            result.onSuccess { events ->
                _events.value = events
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message
                error.printStackTrace()
            }

            _isLoading.value = false
        }
    }
}
