package core.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import theme.dimens
import theme.styles
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSelector(
    dateFrom: Instant?,
    dateTo: Instant?,
    onDateFromChange: (Instant) -> Unit,
    onDateToChange: (Instant) -> Unit,
    modifier: Modifier = Modifier,
) {
    val now = remember {
        val now = ZonedDateTime.now()
        ZonedDateTime
            .of(now.year, now.monthValue, now.dayOfMonth, 0, 0, 0, 0, ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
    }
    val plusMonth = remember { ZonedDateTime.now().plusMonths(1).toInstant().toEpochMilli() }

    Column(modifier = modifier) {
        Text("Date", style = styles.titleSmall)
        Spacer(Modifier.height(dimens.eight))
        Row(horizontalArrangement = Arrangement.spacedBy(dimens.sixteen)) {
            AppDatePicker(
                modifier = Modifier.weight(1f),
                selectedDate = dateFrom,
                selectableDates = selectableDatesFrom(now, plusMonth, dateTo),
                onDateChange = { onDateFromChange(it) },
            )

            AppDatePicker(
                modifier = Modifier.weight(1f),
                selectedDate = dateTo,
                selectableDates = selectableDatesTo(now, plusMonth, dateFrom),
                onDateChange = { onDateToChange(it.plusSeconds(TWENTY_THREE_HOURS_IN_SECONDS)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun selectableDatesFrom(now: Long, plusMonth: Long, dateTo: Instant?) = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        utcTimeMillis in now..plusMonth && (dateTo == null || utcTimeMillis < dateTo.toEpochMilli())
}

@OptIn(ExperimentalMaterial3Api::class)
fun selectableDatesTo(now: Long, plusMonth: Long, dateFrom: Instant?) = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis in now..plusMonth && (dateFrom == null || utcTimeMillis > dateFrom.toEpochMilli())
    }
}

const val TWENTY_THREE_HOURS_IN_SECONDS = (23 * 60 + 59) * 60L
