package com.vazh2100.feature_events.presentaion.screen.event_list.widget

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.vazh2100.core.domain.entities.LocationStatus
import com.vazh2100.core.presentaion.widget.ErrorPanel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun LocationStatusBar(
    locationStatus: LocationStatus,
    context: Context,
) {
    val locationPermissionState = rememberPermissionState(ACCESS_FINE_LOCATION)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
            )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedVisibility(locationStatus == LocationStatus.PERMISSION_DENIED || locationStatus == LocationStatus.LOCATION_OFF) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    ErrorPanel(locationStatus.statusMessage)
                }
            }
            AnimatedVisibility(locationStatus == LocationStatus.PERMISSION_DENIED) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            locationPermissionState.launchPermissionRequest()
                        }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Grant Location Permission")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri =
                                android.net.Uri.fromParts("package", context.packageName, null)
                            intent.data = uri
                            context.startActivity(intent)
                        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Go to Settings")
                    }
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}