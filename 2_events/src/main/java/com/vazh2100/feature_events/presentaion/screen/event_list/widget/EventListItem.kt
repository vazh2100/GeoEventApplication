package com.vazh2100.feature_events.presentaion.screen.event_list.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vazh2100.core.domain.entities.formatter.toLocalFormattedString
import com.vazh2100.feature_events.domain.entities.event.Event
import com.vazh2100.geolocation.entity.GPoint
import com.vazh2100.theme.dimens
import com.vazh2100.theme.styles

@Composable
internal fun EventListItem(userGPoint: GPoint?, event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimens.sixteen,
                vertical = dimens.eight
            )
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(dimens.four)
    ) {
        Column(
            Modifier
                .padding(dimens.sixteen)
        ) {
            Text(
                text = event.name,
                style = styles.titleLarge
            )
            Text(
                text = "Date: ${event.date.toLocalFormattedString()}",
                style = styles.bodyMedium
            )
            Text(
                text = "Type: ${event.type.displayName}",
                style = styles.bodyMedium
            )
            userGPoint?.let {
                Text(
                    text = "Distance: %.2f km".format(event.gPoint.distanceTo(it)),
                    style = styles.bodyMedium
                )
            }
        }
    }
}
