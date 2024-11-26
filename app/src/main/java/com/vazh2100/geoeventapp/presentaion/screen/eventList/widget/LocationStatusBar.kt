package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
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
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.vazh2100.geoeventapp.domain.entities.GPoint
import com.vazh2100.geoeventapp.domain.entities.LocationStatus

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationStatusBar(
    locationStatus: LocationStatus, userGPoint: GPoint?, context: Context
) {
    val locationPermissionState = rememberPermissionState(ACCESS_FINE_LOCATION)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            if (locationStatus == LocationStatus.PERMISSION_DENIED || locationStatus == LocationStatus.LOCATION_OFF) {
                Text(
                    text = "Location Status: ${locationStatus.statusMessage}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            userGPoint?.let {
                Text(
                    text = "Coordinates: Lat: %.2f, Lon: %.2f".format(it.lat, it.lon),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            } ?: Text(
                text = "Coordinates: Not Available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )


            if (locationStatus == LocationStatus.PERMISSION_DENIED) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        locationPermissionState.launchPermissionRequest()
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Grant Location Permission")
                }
            }


            if (!locationPermissionState.status.isGranted) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = android.net.Uri.fromParts("package", context.packageName, null)
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
    }
}