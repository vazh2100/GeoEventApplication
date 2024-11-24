package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vazh2100.geoeventapp.domain.entities.formatter.formatAsUtc
import com.vazh2100.geoeventapp.domain.entities.formatter.toInstance
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

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