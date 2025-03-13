package events.screen.event_list.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import core.widgets.ErrorMessage
import core.widgets.LoadingIndicator
import events.entities.Event
import geolocation.entity.GPoint

@Composable
fun Content(
    isLoading: State<Boolean>,
    errorMessage: State<String?>,
    events: State<List<Event>>,
    userGPoint: GPoint?,
    onEventTap: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        isLoading.value -> LoadingIndicator()
        errorMessage.value != null -> ErrorMessage(errorMessage.value)
        events.value.isEmpty() -> ErrorMessage("No events available")
        else -> EventList(
            events = events.value,
            userGPoint = userGPoint,
            onEventTap = onEventTap,
            modifier = modifier,
        )
    }
}
