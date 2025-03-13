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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    showFilterPanel: MutableState<Boolean>,
    searchParamsState: State<EventSearchParams>,
    userGPoint: GPoint?,
    onApply: (EventSearchParams) -> Unit,
) {
    val searchParams = searchParamsState.value

    // temp params
    val type = remember(showFilterPanel) { mutableStateOf(searchParams.type) }
    val sortType = remember(showFilterPanel) {
        searchParams.sortType.takeIf { it != EventSortType.DISTANCE || searchParams.gPoint != null }.let {
            mutableStateOf(it)
        }
    }
    val dateFrom = remember(showFilterPanel) { mutableStateOf(searchParams.startDate) }
    val dateTo = remember(showFilterPanel) { mutableStateOf(searchParams.endDate) }
    val radius = remember(showFilterPanel) { mutableStateOf(searchParams.radius) }

    AnimatedVisibility(
        enter = expandIn(),
        exit = shrinkOut(),
        visible = showFilterPanel.value,
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
                        currentSelection = type,
                        items = mutableListOf<EventType?>().apply {
                            add(null)
                            addAll(EventType.entries)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    TypeSelector(
                        label = "Sort Type",
                        currentSelection = sortType,
                        items = buildList {
                            add(null)
                            addAll(EventSortType.entries)
                            if (userGPoint == null) remove(EventSortType.DISTANCE)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(dimens.eight))
                DateRangeSelector(
                    dateFrom = dateFrom,
                    dateTo = dateTo,
                )
                userGPoint?.let {
                    Spacer(Modifier.height(dimens.sixteen))
                    RadiusSelector(selectedRadius = radius)
                }
                Spacer(modifier = Modifier.height(dimens.eight))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    OutlinedButton(
                        onClick = { showFilterPanel.value = false },
                        content = { Text("Cancel") }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            onApply(
                                EventSearchParams(
                                    type = type.value,
                                    startDate = dateFrom.value,
                                    endDate = dateTo.value,
                                    radius = radius.value,
                                    sortType = sortType.value,
                                    gPoint = userGPoint,
                                )
                            )
                            showFilterPanel.value = false
                        },
                        content = { Text("Apply") },
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
