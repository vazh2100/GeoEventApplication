package com.vazh2100.geoeventapp.presentaion.screen.eventList

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vazh2100.geoeventapp.presentaion.screen.eventList.widget.*
import org.koin.androidx.compose.koinViewModel

/**
 * The screen includes a top bar, network and location status indicators, a filter panel,
 * and a list of events.
 * @param navController Navigation controller used for navigating between screens.
 * @param viewModel ViewModel for the Event List Screen, provided by Koin dependency injection.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController, viewModel: EventListViewModel = koinViewModel()
) {
    // Observing state from the ViewModel
    val networkStatus by viewModel.networkStatus.collectAsState()
    val locationStatus by viewModel.locationStatus.collectAsState()
    val userGPoint by viewModel.userGPoint.collectAsState()
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val filter by viewModel.filter.collectAsState()
    // Local state for managing temporary filters and visibility of the filter panel
    var tempFilter by remember { mutableStateOf(filter) }
    var showFilterPanel by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = userGPoint?.let {
                        "Coordinates: Lat: %.2f, Lon: %.2f".format(it.lat, it.lon)
                    } ?: "Coordinates: Not Available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                )
            },
            actions = {
                IconButton(onClick = {
                    showFilterPanel = !showFilterPanel
                    // Reset temporary filter when hiding the filter panel
                    if (!showFilterPanel) {
                        tempFilter = filter
                    }
                }) {
                    Icon(
                        Icons.Filled.FilterList,
                        contentDescription = "Filters",
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            },
            expandedHeight = 36.dp
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Displays the current network status
                NetworkStatusBar(networkStatus = networkStatus)
                // Displays the current location status
                LocationStatusBar(
                    locationStatus = locationStatus,
                    context = LocalContext.current
                )
                when {
                    isLoading -> LoadingIndicator()
                    errorMessage != null -> ErrorMessage(errorMessage)
                    events.isEmpty() -> EmptyMessage()
                    else -> EventList(
                        events = events, userGPoint = userGPoint, navController = navController
                    ) // Displays the list of events
                }
            }
            // Filter panel for adjusting the event filters
            FilterPanel(
                showFilterPanel = showFilterPanel,
                filter = filter,
                tempFilter = tempFilter,
                userGPoint = userGPoint,
                setVisibility = { showFilterPanel = it },
                onApplyFilter = viewModel::applyFilters,
            )
        }
    }
}












