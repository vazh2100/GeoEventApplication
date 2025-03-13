package events.screen.event_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import events.entities.Event
import events.screen.event_list.widget.Content
import events.screen.event_list.widget.FilterPanel
import events.screen.event_list.widget.GAppBar
import events.screen.event_list.widget.LocationStatusBar
import network.widget.NetworkStatusBar
import org.koin.androidx.compose.koinViewModel

/**
 * The screen includes a top bar, network and location status indicators, a filter panel,
 * and a list of events.
 */
@Composable
fun EventListScreen(
    onEventTap: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: EventListViewModel = koinViewModel()
    //
    val networkStatus = viewModel.networkStatus.collectAsState()
    val locationStatus = viewModel.locationStatus.collectAsState()
    val userGPoint by viewModel.userGPoint.collectAsState()
    val events = viewModel.events.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    val eventSearchParams = viewModel.eventSearchParams.collectAsState()
    //
    val showFilterPanel = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            GAppBar(
                userGPoint = userGPoint,
                onFilterPress = { showFilterPanel.value = !showFilterPanel.value },
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                content = {
                    Column(modifier = Modifier.fillMaxSize()) {
                        NetworkStatusBar(networkStatus = networkStatus)
                        LocationStatusBar(locationStatus = locationStatus, context = LocalContext.current)
                        Content(
                            isLoading = isLoading,
                            errorMessage = errorMessage,
                            events = events,
                            userGPoint = userGPoint,
                            onEventTap = onEventTap
                        )
                    }
                    FilterPanel(
                        showFilterPanel = showFilterPanel,
                        searchParamsState = eventSearchParams,
                        userGPoint = userGPoint,
                        onApply = viewModel::applyFilters,
                    )
                },
            )
        },
    )
}
