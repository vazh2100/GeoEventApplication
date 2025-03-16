package network.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import core.widgets.ErrorPanel
import network.entity.NetworkStatus
import theme.dimens

@Composable
fun NetworkStatusBar(networkStatus: State<NetworkStatus>, modifier: Modifier = Modifier) {
    val visible = remember { derivedStateOf { networkStatus.value == NetworkStatus.DISCONNECTED } }
    AnimatedVisibility(
        visible = visible.value,
        modifier = modifier.fillMaxWidth()
    ) {
        ErrorPanel(
            text = "No network",
            modifier = Modifier.padding(
                start = dimens.sixteen,
                end = dimens.sixteen,
                top = dimens.eight,
                bottom = dimens.four
            )
        )
    }
}
