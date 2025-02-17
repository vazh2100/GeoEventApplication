package com.vazh2100.feature_events.presentaion.screen.event_list.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RadiusSelector(
    initialRadius: Int?,
    onValueChange: (Int?) -> Unit,
) {
    var tempRadius by remember { mutableStateOf(initialRadius?.toFloat()) }
    Column {
        Text(
            "Distance (km)",
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.height(8.dp))
        Slider(
            onValueChange = { tempRadius = it },
            valueRange = 250f..7500f,
            onValueChangeFinished = { onValueChange(tempRadius?.toInt()) },
            value = tempRadius ?: 7500f,
            steps = 28,
            modifier = Modifier.padding(horizontal = 4.dp),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            },
        )
        tempRadius?.let {
            Spacer(Modifier.height(8.dp))
            Text("Chosen: ${it.toInt()} km")
        }
    }
}
