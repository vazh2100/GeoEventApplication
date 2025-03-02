package network.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import core.widgets.ErrorPanel
import network.entity.NetworkStatus
import theme.dimens

@Composable
fun NetworkStatusBar(networkStatus: NetworkStatus, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        networkStatus == NetworkStatus.DISCONNECTED,
        modifier = modifier.fillMaxWidth()
    ) {
        ErrorPanel(
            "No network",
            modifier = Modifier.padding(
                start = dimens.sixteen,
                end = dimens.sixteen,
                top = dimens.eight,
                bottom = dimens.four
            )
        )
    }
}
