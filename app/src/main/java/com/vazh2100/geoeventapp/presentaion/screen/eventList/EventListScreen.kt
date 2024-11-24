package com.vazh2100.geoeventapp.presentaion.screen.eventList


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.vazh2100.geoeventapp.presentaion.screen.eventList.widget.*
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController, viewModel: EventListViewModel = koinViewModel()
) {
    val networkStatus by viewModel.networkStatus.collectAsState()
    val locationStatus by viewModel.locationStatus.collectAsState()
    val userCoordinates by viewModel.geoPosition.collectAsState()
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val filter by viewModel.filter.collectAsState()

    var tempFilter by remember { mutableStateOf(filter) }
    var showFilterPanel by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Event List") }, actions = {
            IconButton(onClick = {
                showFilterPanel = !showFilterPanel
                if (!showFilterPanel) {
                    tempFilter = filter
                }
            }) {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Filters")
            }
        })
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()) {
                NetworkStatusBar(networkStatus = networkStatus)
                LocationStatusBar(
                    locationStatus = locationStatus,
                    geoPosition = userCoordinates,
                    context = LocalContext.current
                )
                FilterPanel(
                    showFilterPanel = showFilterPanel,
                    filter = filter,
                    tempFilter = tempFilter,
                    userLocation = userCoordinates,
                    setVisibility = { showFilterPanel = it },
                    onApplyFilter = viewModel::applyFilters
                )

                when {
                    isLoading -> LoadingIndicator()
                    errorMessage != null -> ErrorMessage(errorMessage)
                    events.isEmpty() -> EmptyMessage()
                    else -> EventList(
                        events = events,
                        userCoordinates = userCoordinates,
                        navController = navController
                    )
                }
            }
        }
    }
}












