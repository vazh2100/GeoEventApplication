package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

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
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.GPoint
import com.vazh2100.geoeventapp.domain.entities.formatter.toLocalFormattedString

@Composable
fun EventListItem(userGPoint: GPoint?, event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = event.name, style = MaterialTheme.typography.titleLarge
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