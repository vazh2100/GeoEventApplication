package com.vazh2100.core.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.vazh2100.core.entities.DateFormatter.formatAsUtc
import com.vazh2100.core.entities.DateFormatter.toInstance
import com.vazh2100.theme.colors
import com.vazh2100.theme.dimens
import com.vazh2100.theme.shapes
import com.vazh2100.theme.styles
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

const val TWENTY_THREE_HOURS_IN_SECONDS = (23 * 60 + 59) * 60L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSelector(
    dateFrom: Instant?,
    dateTo: Instant?,
    onDateFromChange: (Instant) -> Unit,
    onDateToChange: (Instant) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePickerFrom by remember { mutableStateOf(false) }
    var showDatePickerTo by remember { mutableStateOf(false) }
    val now = ZonedDateTime.now().let {
        ZonedDateTime.of(
            /* year = */
            it.year,
            /* month = */
            it.monthValue,
            /* dayOfMonth = */
            it.dayOfMonth,
            /* hour = */
            0,
            /* minute = */
            0,
            /* second = */
            0,
            /* nanoOfSecond = */
            0,
            /* zone = */
            ZoneId.of("UTC")
        )
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
        DatePickerDialog(
            onDismissRequest = { showDatePickerFrom = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerStateFrom.selectedDateMillis?.let {
                        onDateFromChange(it.toInstance())
                    }
                    showDatePickerFrom = false
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerFrom = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerStateFrom)
        }
    }
    if (showDatePickerTo) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerTo = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerStateTo.selectedDateMillis?.let {
                            onDateToChange(it.toInstance().plusSeconds(TWENTY_THREE_HOURS_IN_SECONDS))
                        }
                        showDatePickerTo = false
                    }
                ) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerTo = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerStateTo)
        }
    }
    Column(modifier = modifier) {
        Text(
            "Date",
            style = styles.titleSmall
        )
        Spacer(Modifier.height(dimens.eight))
        Row(horizontalArrangement = Arrangement.spacedBy(dimens.sixteen)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        dimens.one,
                        colors.onSurface.copy(alpha = 0.5f),
                        shapes.small
                    )
                    .padding(dimens.sixteen)
                    .clickable {
                        showDatePickerFrom = true
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = dateFrom?.formatAsUtc() ?: "",
                        style = styles.bodyLarge,
                        color = colors.onSurface
                    )
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Select from date"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        dimens.one,
                        colors.onSurface.copy(alpha = 0.5f),
                        shapes.small
                    )
                    .padding(dimens.sixteen)
                    .clickable {
                        showDatePickerTo = true
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = dateTo?.formatAsUtc() ?: "",
                        style = styles.bodyLarge,
                        color = colors.onSurface
                    )
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Select date to"
                    )
                }
            }
        }
    }
}
