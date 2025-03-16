package core.widgets

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
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import core.entities.DateFormatter.formatAsUtc
import core.entities.DateFormatter.toInstance
import theme.colors
import theme.dimens
import theme.shapes
import theme.styles
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePicker(
    selectedDate: State<Instant?>,
    selectableDates: SelectableDates,
    onDateChange: (Instant) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.value?.toEpochMilli(),
        initialDisplayedMonthMillis = selectedDate.value?.toEpochMilli(),
        selectableDates = selectableDates,
    )
    Box(
        modifier = modifier
            .border(dimens.one, colors.onSurface.copy(alpha = 0.5f), shapes.small)
            .padding(dimens.sixteen)
            .clickable { showDatePicker = true },
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = selectedDate.value?.formatAsUtc() ?: "",
                    style = styles.bodyLarge,
                    color = colors.onSurface,
                )
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Select date",
                )
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onDateChange(it.toInstance()) }
                        showDatePicker = false
                    },
                    content = { Text("Ok") },
                )
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    content = { Text("Cancel") },
                )
            },
            content = { DatePicker(state = datePickerState) },
        )
    }
}
