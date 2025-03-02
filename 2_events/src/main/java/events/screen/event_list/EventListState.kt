package events.screen.event_list

import androidx.lifecycle.ViewModel
import events.entities.Event
import events.entities.EventSearchParams
import geolocation.entity.GPoint
import geolocation.entity.LocationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import network.entity.NetworkStatus

@Suppress("PropertyName", "VariableNaming")
internal abstract class EventListState : ViewModel() {
    // currently applied event filter
    protected val _searchParams = MutableStateFlow(EventSearchParams())
    val eventSearchParams = _searchParams.asStateFlow()

    // list of events to be displayed
    protected val _events = MutableStateFlow<List<Event>>(emptyList())
    val events = _events.asStateFlow()

    // location status and current user's coordinates
    lateinit var locationStatus: StateFlow<LocationStatus>
        protected set
    lateinit var userGPoint: StateFlow<GPoint?>
        protected set
    lateinit var networkStatus: StateFlow<NetworkStatus>
        protected set

    protected val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    protected val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()
}
