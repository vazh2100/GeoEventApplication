package com.vazh2100.feature_events.presentaion.screen.event_list

import androidx.lifecycle.viewModelScope
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams
import com.vazh2100.feature_events.domain.usecase.GetFilteredEventsUseCase
import com.vazh2100.feature_events.domain.usecase.GetSavedFiltersUseCase
import com.vazh2100.geolocation.entity.LocationStatus.UNDEFINED
import com.vazh2100.geolocation.usecase.IGetLocationStatusUseCase
import com.vazh2100.network.entity.NetworkStatus.UNKNOWN
import com.vazh2100.network.usecase.IObserveNetworkStateUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class EventListViewModel(
    private val getSavedFilters: GetSavedFiltersUseCase,
    private val getFilteredEvents: GetFilteredEventsUseCase,
    getNetworkStatus: IObserveNetworkStateUseCase,
    getLocationStatus: IGetLocationStatusUseCase,
) : EventListState() {

    init {
        locationStatus = getLocationStatus().stateIn(viewModelScope, SharingStarted.Eagerly, UNDEFINED)
        userGPoint = getLocationStatus.userGPoint
        networkStatus = getNetworkStatus().stateIn(viewModelScope, SharingStarted.Eagerly, UNKNOWN)
        viewModelScope.launch {
            loadSavedFilters()
            loadEvents()
            reactOnUserGPChanged()
        }
    }

    fun applyFilters(eventSearchParams: EventSearchParams) {
        val changed = eventSearchParams != _searchParams.value
        if (changed) {
            _searchParams.value = eventSearchParams
            viewModelScope.launch {
                loadEvents()
            }
        }
    }

    private suspend fun loadSavedFilters() {
        _isLoading.value = true
        getSavedFilters().onSuccess {
            _searchParams.value = it
        }.onFailure {
            _errorMessage.value = it.message
        }
        _isLoading.value = false
    }

    /** Loads the events based on the current filter. */
    private suspend fun loadEvents() {
        _isLoading.value = true
        getFilteredEvents(_searchParams.value).onSuccess { events ->
            _events.value = events
            _errorMessage.value = null
        }.onFailure { error ->
            _errorMessage.value = error.message
        }
        _isLoading.value = false
    }

    /** If there is a filter, and GPoint appeared after it was not there, then we reload the events. */
    private fun reactOnUserGPChanged() = userGPoint.scan(userGPoint.value) { old, new ->
        val isFromNull = old == null && new != null
        val hasGeoFilter = _searchParams.value.hasGeoFilter
        if (isFromNull && hasGeoFilter) {
            loadEvents()
        }
        new
    }.launchIn(viewModelScope)
}
