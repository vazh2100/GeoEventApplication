package com.vazh2100.geoeventapp.presentaion.screen.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventType
import com.vazh2100.geoeventapp.domain.usecase.GetFilteredEventsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class EventListViewModel(
    private val getFilteredEventsUseCase: GetFilteredEventsUseCase
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> get() = _events

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        loadEvents()
    }

    fun loadEvents(
        type: EventType? = null,
        startDate: ZonedDateTime? = null,
        endDate: ZonedDateTime? = null,
        radius: Double? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getFilteredEventsUseCase.execute(
                type = type,
                startDate = startDate,
                endDate = endDate,
                radius = radius
            )
            result.onSuccess {
                _events.value = it
                _errorMessage.value = null
            }.onFailure { error ->
                _errorMessage.value = error.message
                println(error.stackTrace)
            }
            _isLoading.value = false
        }
    }
}
