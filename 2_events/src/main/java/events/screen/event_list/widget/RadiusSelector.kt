package events.screen.event_list.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import theme.colors
import theme.dimens
import theme.styles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RadiusSelector(
    initialRadius: Int?,
    onValueChange: (Int?) -> Unit,
) {
    var tempRadius by remember { mutableStateOf(initialRadius?.toFloat()) }
    Column {
        Text("Distance (km)", style = styles.titleSmall)
        Spacer(Modifier.height(dimens.eight))
        Slider(
            onValueChange = { tempRadius = it },
            valueRange = 250f..7500f,
            onValueChangeFinished = { onValueChange(tempRadius?.toInt()) },
            value = tempRadius ?: 7500f,
            steps = 28,
            modifier = Modifier.padding(horizontal = dimens.four),
            thumb = { Box(modifier = Modifier.size(dimens.sixteen).background(colors.primary, shape = CircleShape)) },
        )
        tempRadius?.let {
            Spacer(Modifier.height(dimens.eight))
            Text("Chosen: ${it.toInt()} km")
        }
    }
}
