package events.screen.event_list.widget

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import core.widgets.DateRangeSelector
import core.widgets.TypeSelector
import events.entities.EventSearchParams
import events.entities.EventSortType
import events.entities.EventType
import geolocation.entity.GPoint
import theme.dimens
import theme.shapes

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
                .padding(horizontal = dimens.sixteen, vertical = dimens.eight),
            shadowElevation = dimens.four,
            tonalElevation = dimens.four,
            shape = shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(dimens.sixteen),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    TypeSelector(
                        label = "Event Type",
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
                    TypeSelector(
                        label = "Sort Type",
                        currentSelection = currentSelection,
                        items = buildList {
                            add(null)
                            addAll(EventSortType.entries)
                            if (userGPoint == null) remove(EventSortType.DISTANCE)
                        },
                        onSelectionChange = { tempParams = tempParams.copy(sortType = it) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(dimens.eight))
                DateRangeSelector(
                    dateFrom = tempParams.startDate,
                    dateTo = tempParams.endDate,
                    onDateFromChange = { tempParams = tempParams.copy(startDate = it) },
                    onDateToChange = { tempParams = tempParams.copy(endDate = it) }
                )
                userGPoint?.let {
                    Spacer(Modifier.height(dimens.sixteen))
                    RadiusSelector(
                        initialRadius = tempParams.radius,
                        onValueChange = { tempParams = tempParams.copy(radius = it) }
                    )
                }
                Spacer(modifier = Modifier.height(dimens.eight))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    OutlinedButton(
                        onClick = onClose,
                        content = { Text("Cancel") }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { onClose(); onApply(tempParams) },
                        content = { Text("Apply") },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
