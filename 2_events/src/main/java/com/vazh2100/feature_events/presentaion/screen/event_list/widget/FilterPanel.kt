package com.vazh2100.feature_events.presentaion.screen.event_list.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vazh2100.core.domain.entities.GPoint
import com.vazh2100.core.presentaion.widget.DateRangeSelector
import com.vazh2100.core.presentaion.widget.TypeSelector
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams
import com.vazh2100.feature_events.domain.entities.event.EventSortType
import com.vazh2100.feature_events.domain.entities.event.EventType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FilterPanel(
    showFilterPanel: Boolean,
    searchParams: EventSearchParams,
    onClose: () -> Unit,
    onApply: (EventSearchParams) -> Unit,
    userGPoint: GPoint?,
) {
    var tempParams by remember { mutableStateOf(searchParams) }

    if (!showFilterPanel) tempParams = searchParams
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
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    TypeSelector<EventType>(
                        currentSelection = tempParams.type,
                        onSelectionChange = { tempParams = tempParams.copy(type = it) },
                        items = mutableListOf<EventType?>().apply {
                            add(null)
                            addAll(EventType.entries)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    val currentSelection =
                        tempParams.sortType.takeIf { it != EventSortType.DISTANCE || userGPoint != null }
                    TypeSelector<EventSortType>(
                        currentSelection = currentSelection,
                        items = mutableListOf<EventSortType?>().apply {
                            add(null)
                            addAll(EventSortType.entries)
                            if (userGPoint == null) remove(EventSortType.DISTANCE)
                        },
                        onSelectionChange = {
                            tempParams = tempParams.copy(sortType = it)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                DateRangeSelector(
                    dateFrom = tempParams.startDate,
                    dateTo = tempParams.endDate,
                    onDateFromChange = { tempParams = tempParams.copy(startDate = it) },
                    onDateToChange = { tempParams = tempParams.copy(endDate = it) }
                )
                userGPoint?.let {
                    Spacer(Modifier.height(16.dp))
                    RadiusSelector(
                        initialRadius = tempParams.radius,
                        onValueChange = {
                            tempParams = tempParams.copy(radius = it)
                        }
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    OutlinedButton(onClick = {
                        onClose()
                    }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        onClose()
                        onApply(tempParams)
                    }) {
                        Text("Apply")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
