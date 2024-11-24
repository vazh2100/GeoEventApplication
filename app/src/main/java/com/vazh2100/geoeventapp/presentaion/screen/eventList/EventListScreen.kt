package com.vazh2100.geoeventapp.presentaion.screen.eventList


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.vazh2100.geoeventapp.domain.entities.EventFilter
import com.vazh2100.geoeventapp.domain.entities.EventType
import com.vazh2100.geoeventapp.domain.entities.LocationStatus
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus
import com.vazh2100.geoeventapp.domain.entities.formatter.formatAsUtc
import com.vazh2100.geoeventapp.domain.entities.formatter.toInstance
import com.vazh2100.geoeventapp.presentaion.screen.eventList.widget.EventListItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.getViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController, viewModel: EventListViewModel = getViewModel()
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
                if (showFilterPanel == false) {
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


                FilterPanel(showFilterPanel = showFilterPanel,
                    filter = filter,
                    tempFilter = tempFilter,
                    userLocation = userCoordinates,
                    setVisibility = {
                        showFilterPanel = it
                    },

                    onApplyFilter = {
                        viewModel.applyFilters(it)
                    })


                when {
                    isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = errorMessage ?: "Download Failed")
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(events) { event ->
                                EventListItem(event = event, onClick = {
                                    navController.navigate("eventDetails/${Json.encodeToString(event)}")
                                }, userCoordinates = userCoordinates)
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationStatusBar(
    locationStatus: LocationStatus, geoPosition: Pair<Double, Double>?, context: Context
) {
    val locationPermissionState = rememberPermissionState(ACCESS_FINE_LOCATION)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            if (locationStatus == LocationStatus.PERMISSION_DENIED || locationStatus == LocationStatus.LOCATION_OFF) {
                Text(
                    text = "Location Status: ${locationStatus.statusMessage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            geoPosition?.let {
                Text(
                    text = "Coordinates: Lat: %.2f, Lon: %.2f".format(it.first, it.second),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            } ?: Text(
                text = "Coordinates: Not Available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )


            if (locationStatus == LocationStatus.PERMISSION_DENIED) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        locationPermissionState.launchPermissionRequest()
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Grant Location Permission")
                }
            }


            if (!locationPermissionState.status.isGranted) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = android.net.Uri.fromParts("package", context.packageName, null)
                        intent.data = uri
                        context.startActivity(intent)
                    }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Go to Settings")
                }
            }
        }
    }
}


@Composable
fun NetworkStatusBar(networkStatus: NetworkStatus) {
    val backgroundColor = when (networkStatus) {
        NetworkStatus.CONNECTED -> MaterialTheme.colorScheme.primary
        NetworkStatus.DISCONNECTED -> MaterialTheme.colorScheme.error
        NetworkStatus.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when (networkStatus) {
        NetworkStatus.CONNECTED -> MaterialTheme.colorScheme.onPrimary
        NetworkStatus.DISCONNECTED -> MaterialTheme.colorScheme.onError
        NetworkStatus.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    if (networkStatus == NetworkStatus.DISCONNECTED) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(8.dp)
        ) {
            Text(
                text = when (networkStatus) {
                    else -> "No network"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterPanel(
    showFilterPanel: Boolean,
    filter: EventFilter,
    tempFilter: EventFilter,
    setVisibility: (Boolean) -> Unit,
    onApplyFilter: (EventFilter) -> Unit,
    userLocation: Pair<Double, Double>?

) {
    var tempFilter by remember { mutableStateOf(tempFilter) }

    if (showFilterPanel) {
        tempFilter = filter
    }

    AnimatedVisibility(
        visible = showFilterPanel, modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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

                Text(
                    "Distance (km)", style = MaterialTheme.typography.titleSmall
                )
                Slider(
                    value = tempFilter.radius?.toFloat() ?: 17001f,
                    onValueChange = {
                        tempFilter = tempFilter.copy(radius = it.toInt())
                    },
                    valueRange = 1f..17001f,
                    steps = 30,
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
                    enabled = userLocation != null
                )
                Text("Chosen: ${tempFilter.radius?.toInt() ?: 17001} km")


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

@Composable
fun EventTypeSelector(
    currentSelection: EventType?, onSelectionChange: (EventType?) -> Unit, items: List<EventType?>
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(currentSelection?.displayName ?: "Choose type")
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { type ->
                DropdownMenuItem(onClick = {
                    onSelectionChange(type)
                    expanded = false
                }, text = { Text(type?.displayName ?: "None") })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSelector(
    dateFrom: Instant?,
    dateTo: Instant?,
    onDateFromChange: (Instant) -> Unit,
    onDateToChange: (Instant) -> Unit
) {
    var showDatePickerFrom by remember { mutableStateOf(false) }
    var showDatePickerTo by remember { mutableStateOf(false) }

    val now = ZonedDateTime.now().let {
        ZonedDateTime
            .of(it.year, it.monthValue, it.dayOfMonth, 0, 0, 0, 0, ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
    }
    val plusMonth = ZonedDateTime.now().plusMonths(1).toInstant().toEpochMilli()

    val selectableDatesFrom = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis in now..plusMonth && (dateTo == null || utcTimeMillis < dateTo.toEpochMilli())
        }
    }

    val selectableDatesTo = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis in now..plusMonth && (dateFrom == null || utcTimeMillis > dateFrom.toEpochMilli())
        }
    }

    val datePickerStateFrom = rememberDatePickerState(
        initialSelectedDateMillis = dateFrom?.toEpochMilli(),
        initialDisplayedMonthMillis = dateFrom?.toEpochMilli(),
        selectableDates = selectableDatesFrom,
    )

    val datePickerStateTo = rememberDatePickerState(
        initialSelectedDateMillis = dateTo?.toEpochMilli(),
        initialDisplayedMonthMillis = dateTo?.toEpochMilli(),
        selectableDates = selectableDatesTo
    )

    if (showDatePickerFrom) {
        DatePickerDialog(onDismissRequest = { showDatePickerFrom = false }, confirmButton = {

            TextButton(onClick = {
                datePickerStateFrom.selectedDateMillis?.let {
                    onDateFromChange(it.toInstance())
                }
                showDatePickerFrom = false
            }) {
                Text("Ok")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePickerFrom = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerStateFrom)
        }
    }


    if (showDatePickerTo) {
        DatePickerDialog(onDismissRequest = { showDatePickerTo = false }, confirmButton = {
            TextButton(onClick = {
                datePickerStateTo.selectedDateMillis?.let {
                    onDateToChange(it.toInstance().plusSeconds((23 * 60 + 59) * 60))
                }
                showDatePickerTo = false
            }) {
                Text("Ok")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePickerTo = false }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerStateTo)
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier
            .weight(1f)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                MaterialTheme.shapes.small
            )
            .padding(16.dp)
            .clickable {
                showDatePickerFrom = true
            }) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFrom?.formatAsUtc() ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Filled.DateRange, contentDescription = "Select from date"
                )
            }
        }

        Box(modifier = Modifier
            .weight(1f)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                MaterialTheme.shapes.small
            )
            .padding(16.dp)
            .clickable {
                showDatePickerTo = true
            }) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateTo?.formatAsUtc() ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Filled.DateRange, contentDescription = "Select date to"
                )
            }
        }
    }
}







