package events.screen.event_detail

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import core.entities.DateFormatter.toLocalFormattedString
import events.entities.Event
import theme.colors
import theme.dimens
import theme.shapes
import theme.styles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    event: Event,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(dimens.sixteen)
        ) {
            Card(
                shape = shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(dimens.four)
            ) {
                Column(
                    modifier = Modifier.padding(dimens.sixteen)
                ) {
                    Text(
                        text = event.name,
                        style = styles.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(dimens.twelve))
                    Text(
                        text = "Date: ${event.date.toLocalFormattedString()}",
                        style = styles.bodyMedium
                    )
                    Spacer(Modifier.height(dimens.eight))
                    Text(
                        text = "Type: ${event.type.displayName}",
                        style = styles.bodyMedium
                    )
                    Spacer(Modifier.height(dimens.eight))

                    Text(
                        text = "Location: ${event.city}",
                        style = styles.bodyMedium
                    )
                    Spacer(Modifier.height(dimens.twelve))
                    Text(
                        text = "Description:",
                        style = styles.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(Modifier.height(dimens.eight))
                    Text(
                        text = event.description,
                        style = MaterialTheme
                            .typography
                            .bodySmall
                            .copy(color = colors.onSurfaceVariant),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(Modifier.height(dimens.sixteen))
            Button(
                onClick = {
                    addEventToGoogleCalendar(
                        event,
                        context
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Calendar")
            }
        }
    }
}

private const val TWO_HOURS_IN_SECONDS = 2 * 60 * 60L

internal fun addEventToGoogleCalendar(event: Event, context: Context) {
    val startMillis = event.date.toEpochMilli()
    val endMillis = event.date.plusSeconds(TWO_HOURS_IN_SECONDS).toEpochMilli()
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, event.name)
        putExtra(CalendarContract.Events.DESCRIPTION, event.description)
        putExtra(CalendarContract.Events.EVENT_LOCATION, "${event.latitude}, ${event.longitude}")
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
