package events.screen.event_list.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import events.entities.Event
import geolocation.entity.GPoint

@Composable
internal fun EventList(
    events: List<Event>,
    userGPoint: GPoint?,
    onEventTap: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(events) { event ->
            EventListItem(
                event = event,
                onClick = { onEventTap(event) },
                userGPoint = userGPoint
            )
        }
    }
}
