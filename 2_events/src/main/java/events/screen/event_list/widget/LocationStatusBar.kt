package events.screen.event_list.widget

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import core.widgets.ErrorPanel
import geolocation.entity.LocationStatus
import geolocation.entity.LocationStatus.LOCATION_OFF
import geolocation.entity.LocationStatus.PERMISSION_DENIED
import theme.colors
import theme.dimens

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
            .padding(horizontal = dimens.sixteen)
            .background(colors.surface.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val isVisible = locationStatus == PERMISSION_DENIED || locationStatus == LOCATION_OFF
            AnimatedVisibility(isVisible) {
                Column {
                    Spacer(modifier = Modifier.height(dimens.eight))
                    ErrorPanel(locationStatus.statusMessage)
                }
            }
            AnimatedVisibility(locationStatus == PERMISSION_DENIED) {
                Column {
                    Spacer(modifier = Modifier.height(dimens.eight))
                    Button(
                        onClick = { locationPermissionState.launchPermissionRequest() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "Grant Location Permission")
                    }
                    Spacer(modifier = Modifier.height(dimens.four))
                    Button(
                        onClick = {
                            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = android.net.Uri.fromParts(
                                "package",
                                context.packageName,
                                null
                            )
                            intent.data = uri
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary
                        )
                    ) {
                        Text(text = "Go to Settings")
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimens.eight))
        }
    }
}
