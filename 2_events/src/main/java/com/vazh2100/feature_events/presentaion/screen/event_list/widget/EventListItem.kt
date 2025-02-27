package com.vazh2100.feature_events.presentaion.screen.event_list.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vazh2100.core.domain.entities.formatter.toLocalFormattedString
import com.vazh2100.feature_events.domain.entities.event.Event
import com.vazh2100.geolocation.entity.GPoint

@Composable
internal fun EventListItem(userGPoint: GPoint?, event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
        ) {
            Text(
                text = event.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Date: ${event.date.toLocalFormattedString()}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Type: ${event.type.displayName}",
                style = MaterialTheme.typography.bodyMedium
            )
            userGPoint?.let {
                Text(
                    text = "Distance: %.2f km".format(event.gPoint.distanceTo(it)),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
