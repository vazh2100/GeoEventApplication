package com.vazh2100.geoeventapp.domain.entities

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object LocationPermissionHelper {
    const val REQUIRED_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION

    fun isPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(
        launcher: ActivityResultLauncher<String>
    ) {
        launcher.launch(REQUIRED_PERMISSION)
    }
}