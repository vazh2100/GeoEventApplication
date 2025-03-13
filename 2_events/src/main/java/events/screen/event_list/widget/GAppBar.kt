package events.screen.event_list.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import geolocation.entity.GPoint
import theme.colors
import theme.dimens
import theme.styles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GAppBar(
    userGPoint: GPoint?,
    onFilterPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            val value = remember(userGPoint) {
                userGPoint?.let { "Lat: %.2f, Lon: %.2f".format(it.lat, it.lon) } ?: "Not Available"
            }
            Text(
                text = "Coordinates: $value",
                style = styles.bodyMedium,
                color = colors.onSurface,
                modifier = Modifier.padding(top = dimens.eight, start = dimens.eight)
            )
        },
        actions = {
            IconButton(onClick = onFilterPress) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filters",
                    modifier = Modifier.size(dimens.thirtySix)
                )
            }
            Spacer(modifier = Modifier.width(dimens.eight))
        },
        expandedHeight = dimens.thirtySix,
    )
}
