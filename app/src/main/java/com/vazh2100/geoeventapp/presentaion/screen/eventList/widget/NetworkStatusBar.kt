package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus

@Composable
fun NetworkStatusBar(networkStatus: NetworkStatus) {
    val backgroundColor = when (networkStatus) {
        NetworkStatus.CONNECTED -> MaterialTheme.colorScheme.primary
        NetworkStatus.DISCONNECTED -> MaterialTheme.colorScheme.error
        NetworkStatus.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when (networkStatus) {
        NetworkStatus.CONNECTED -> MaterialTheme.colorScheme.onPrimary
        NetworkStatus.DISCONNECTED -> MaterialTheme.colorScheme.onError
        NetworkStatus.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    if (networkStatus == NetworkStatus.DISCONNECTED) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(8.dp)
        ) {
            Text(
                text = when (networkStatus) {
                    else -> "No network"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}