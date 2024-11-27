package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.EventType
import com.vazh2100.geoeventapp.domain.entities.GPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(
    showFilterPanel: Boolean,
    filter: EventFilter,
    tempFilter: EventFilter,
    setVisibility: (Boolean) -> Unit,
    onApplyFilter: (EventFilter) -> Unit,
    userGPoint: GPoint?,
) {
    var tempFilter by remember { mutableStateOf(tempFilter) }

    if (showFilterPanel) {
        tempFilter = filter
    }

    AnimatedVisibility(
        enter = expandIn(),
        exit = shrinkOut(),
        visible = showFilterPanel,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shadowElevation = 4.dp,
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text("Event Type", style = MaterialTheme.typography.titleSmall)
                EventTypeSelector(currentSelection = tempFilter.type,
                    onSelectionChange = { tempFilter = tempFilter.copy(type = it) },
                    items = EventType.entries.toMutableList<EventType?>().apply { add(null) })


                Text("Date", style = MaterialTheme.typography.titleSmall)
                DateRangeSelector(dateFrom = tempFilter.startDate,
                    dateTo = tempFilter.endDate,
                    onDateFromChange = { tempFilter = tempFilter.copy(startDate = it) },
                    onDateToChange = {
                        tempFilter = tempFilter.copy(endDate = it)
                    })

                userGPoint?.let {
                    Text(
                        "Distance (km)", style = MaterialTheme.typography.titleSmall
                    )
                    Slider(
                        value = tempFilter.radius?.toFloat() ?: 7500f,
                        onValueChange = {
                            tempFilter = tempFilter.copy(radius = it.toInt())
                        },
                        valueRange = 250f..7500f,
                        steps = 28,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary, shape = CircleShape
                                    )
                            )
                        },
                    )
                    tempFilter.radius?.let { Text("Chosen: ${it.toInt()} km") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = {
                        setVisibility(false)
                        tempFilter = filter

                    }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        setVisibility(false)
                        onApplyFilter(tempFilter)
                    }) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}