package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus

@Composable
fun NetworkStatusBar(networkStatus: NetworkStatus) {
    AnimatedVisibility(
        networkStatus == NetworkStatus.DISCONNECTED, modifier = Modifier.fillMaxWidth()
    ) {
        ErrorPanel(
            "No network",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
        )
    }
}