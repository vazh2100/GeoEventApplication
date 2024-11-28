package com.vazh2100.feature_events.presentaion.screen.event_list.widget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vazh2100.core.domain.entities.GPoint
import com.vazh2100.feature_events.domain.entities.event.Event

@Composable
internal fun EventList(
    events: List<Event>, userGPoint: GPoint?,
    onEventTap: (Event) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(events) { event ->
            EventListItem(
                event = event, onClick = {
                    onEventTap(event)
                }, userGPoint = userGPoint
            )
        }
    }
}
