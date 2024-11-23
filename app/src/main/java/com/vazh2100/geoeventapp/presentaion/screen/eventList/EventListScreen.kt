package com.vazh2100.geoeventapp.presentaion.screen.eventList

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vazh2100.geoeventapp.domain.entities.EventType
import com.vazh2100.geoeventapp.domain.entities.formatter.toFormattedMonth
import com.vazh2100.geoeventapp.domain.entities.formatter.toZonedDateTime
import com.vazh2100.geoeventapp.presentaion.screen.eventList.widget.EventListItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.getViewModel
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    navController: NavController, viewModel: EventListViewModel = getViewModel()
) {
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    var showFilterPanel by remember { mutableStateOf(false) }
    var selectedEventType by remember { mutableStateOf<EventType?>(null) }
    var dateFrom by remember { mutableStateOf(ZonedDateTime.now()) }
    var dateTo by remember { mutableStateOf(ZonedDateTime.now().plusMonths(1)) }
    var distance by remember { mutableIntStateOf(15) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Event List") }, actions = {
            IconButton(onClick = { showFilterPanel = !showFilterPanel }) {
                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Фильтры")
            }
        })
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Панель фильтров
                AnimatedVisibility(visible = showFilterPanel) {
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
                            // Фильтр по типу
                            Text("Event Type", style = MaterialTheme.typography.titleSmall)
                            EventTypeSelector(currentSelection = selectedEventType,
                                onSelectionChange = { selectedEventType = it },
                                items = EventType.entries
                                    .toMutableList<EventType?>()
                                    .apply { add(null) })

                            // Фильтр по дате
                            Text("Date", style = MaterialTheme.typography.titleSmall)
                            DateRangeSelector(dateFrom = dateFrom,
                                dateTo = dateTo,
                                onDateFromChange = { dateFrom = it },
                                onDateToChange = { dateTo = it })

                            Text(
                                "Distance (km)", style = MaterialTheme.typography.titleSmall
                            )
                            Slider(value = distance.toFloat(),
                                onValueChange = { distance = it.toInt() },
                                valueRange = 1f..30f,
                                steps = 29,
                                modifier = Modifier.padding(horizontal = 4.dp),
                                thumb = {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp) // Размер точки
                                            .background(
                                                MaterialTheme.colorScheme.primary,
                                                shape = CircleShape
                                            )
                                    )
                                })
                            Text("Chosen: ${distance.toInt()} km")

                            // Кнопки "Применить" и "Отмена"
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedButton(onClick = { showFilterPanel = false }) {
                                    Text("Отмена")
                                }
                                Button(onClick = {
                                    showFilterPanel = false
                                    // Логика применения фильтров
                                    viewModel.applyFilters(
                                        type = selectedEventType,
                                        dateFrom = dateFrom,
                                        dateTo = dateTo,
                                        distance = distance.toInt()
                                    )
                                }) {
                                    Text("Применить")
                                }
                            }
                        }
                    }
                }

                // Список событий
                when {
                    isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    errorMessage != null -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = errorMessage ?: "Ошибка загрузки")
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
                                })
                            }
                        }
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
    dateFrom: ZonedDateTime,
    dateTo: ZonedDateTime,
    onDateFromChange: (ZonedDateTime) -> Unit,
    onDateToChange: (ZonedDateTime) -> Unit
) {
    var showDatePickerFrom by remember { mutableStateOf(false) }
    var showDatePickerTo by remember { mutableStateOf(false) }

    val currentDate = ZonedDateTime.now().toInstant().toEpochMilli()

    val nextMonthDate = ZonedDateTime.now().plusMonths(1).toInstant().toEpochMilli()

    val selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis in currentDate..nextMonthDate
        }
    }


    // Состояние для выбора дат
    val datePickerStateFrom = rememberDatePickerState(
        initialSelectedDateMillis = currentDate,
        initialDisplayedMonthMillis = currentDate,
        selectableDates = selectableDates,
    )

    val datePickerStateTo = rememberDatePickerState(
        initialSelectedDateMillis = currentDate,
        initialDisplayedMonthMillis = currentDate,
        selectableDates = selectableDates
    )

    // Диалог для выбора даты "от"
    if (showDatePickerFrom) {
        DatePickerDialog(onDismissRequest = { showDatePickerFrom = false }, confirmButton = {
            TextButton(onClick = {
                datePickerStateFrom.selectedDateMillis?.let {
                    onDateFromChange(it.toZonedDateTime()) // Обновляем состояние для "от"
                }
                showDatePickerFrom = false
            }) {
                Text("ОК")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePickerFrom = false }) {
                Text("Отмена")
            }
        }) {
            DatePicker(state = datePickerStateFrom)
        }
    }

    // Диалог для выбора даты "до"
    if (showDatePickerTo) {
        DatePickerDialog(onDismissRequest = { showDatePickerTo = false }, confirmButton = {
            TextButton(onClick = {
                datePickerStateTo.selectedDateMillis?.let {
                    onDateToChange(it.toZonedDateTime())
                }
                showDatePickerTo = false
            }) {
                Text("ОК")
            }
        }, dismissButton = {
            TextButton(onClick = { showDatePickerTo = false }) {
                Text("Отмена")
            }
        }) {
            DatePicker(state = datePickerStateTo)
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Блок для выбора "от"
        Box(modifier = Modifier
            .weight(1f)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                MaterialTheme.shapes.small
            )
            .padding(16.dp)
            .clickable {
                showDatePickerFrom = true // Открываем диалог для "от"
            }) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFrom.toFormattedMonth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Filled.DateRange, contentDescription = "Выбрать дату от"
                )
            }
        }

        // Блок для выбора "до"
        Box(modifier = Modifier
            .weight(1f)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                MaterialTheme.shapes.small
            )
            .padding(16.dp)
            .clickable {
                showDatePickerTo = true // Открываем диалог для "до"
            }) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateTo.toFormattedMonth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Filled.DateRange, contentDescription = "Выбрать дату до"
                )
            }
        }
    }
}







