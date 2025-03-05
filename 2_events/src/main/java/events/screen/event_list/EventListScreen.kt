package events.screen.event_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import core.widgets.ErrorMessage
import core.widgets.LoadingIndicator
import events.entities.Event
import events.screen.event_list.widget.EventList
import events.screen.event_list.widget.FilterPanel
import events.screen.event_list.widget.LocationStatusBar
import network.widget.NetworkStatusBar
import org.koin.androidx.compose.koinViewModel
import theme.colors
import theme.dimens
import theme.styles

/**
 * The screen includes a top bar, network and location status indicators, a filter panel,
 * and a list of events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    onEventTap: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: EventListViewModel = koinViewModel()
    //
    val networkStatus by viewModel.networkStatus.collectAsState()
    val locationStatus by viewModel.locationStatus.collectAsState()
    val userGPoint by viewModel.userGPoint.collectAsState()
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val eventSearchParams by viewModel.eventSearchParams.collectAsState()
    //
    var showFilterPanel by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = userGPoint?.let { "Coordinates: Lat: %.2f, Lon: %.2f".format(it.lat, it.lon) }
                        ?: "Coordinates: Not Available",
                    style = styles.bodyMedium,
                    color = colors.onSurface,
                    modifier = Modifier.padding(top = dimens.eight, start = dimens.eight)
                )
            },
            actions = {
                IconButton(onClick = { showFilterPanel = !showFilterPanel }) {
                    Icon(
                        Icons.Filled.FilterList,
                        contentDescription = "Filters",
                        modifier = Modifier.size(dimens.thirtySix)
                    )
                }
                Spacer(modifier = Modifier.width(dimens.eight))
            },
            expandedHeight = dimens.thirtySix,
        )
    }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()) {
                NetworkStatusBar(networkStatus = networkStatus)
                LocationStatusBar(locationStatus = locationStatus, context = LocalContext.current)
                when {
                    isLoading -> LoadingIndicator()
                    errorMessage != null -> ErrorMessage(errorMessage)
                    events.isEmpty() -> ErrorMessage("No events available")
                    else -> EventList(
                        events = events,
                        userGPoint = userGPoint,
                        onEventTap = onEventTap
                    )
                }
            }
            FilterPanel(
                showFilterPanel = showFilterPanel,
                searchParams = eventSearchParams,
                userGPoint = userGPoint,
                onClose = { showFilterPanel = false },
                onApply = viewModel::applyFilters,
            )
        }
    }
}
