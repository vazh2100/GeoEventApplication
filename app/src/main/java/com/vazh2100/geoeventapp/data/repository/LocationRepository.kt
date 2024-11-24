package com.vazh2100.geoeventapp.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.vazh2100.geoeventapp.domain.entities.LocationStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationRepository(private val context: Context) {

    private val _locationStatus = MutableStateFlow(LocationStatus.PERMISSION_DENIED)
    val locationStatus: StateFlow<LocationStatus> = _locationStatus

    private val _currentCoordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentCoordinates: StateFlow<Pair<Double, Double>?> = _currentCoordinates

    private var periodicCheckJob: Job? = null
    private var locationListener: LocationListener? = null

    private val locationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    init {
        startPeriodicPermissionCheck()
    }

    private fun startPeriodicPermissionCheck() {
        periodicCheckJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val currentStatus = checkPermissionStatus()
                _locationStatus.emit(currentStatus)

                if (currentStatus == LocationStatus.PERMISSION_GRANTED) {
                    val enabled = checkGeolocationEnabled()
                    if (!enabled) _locationStatus.emit(LocationStatus.LOCATION_OFF) else _locationStatus.emit(
                        LocationStatus.LOCATION_ON
                    )
                    startLocationUpdates()
                } else {
                    stopLocationUpdates()
                }
                delay(10_000)
            }
        }
    }

    private fun checkPermissionStatus(): LocationStatus {
        val isPermissionGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return if (isPermissionGranted) {
            LocationStatus.PERMISSION_GRANTED
        } else {
            LocationStatus.PERMISSION_DENIED
        }
    }

    private fun checkGeolocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun startLocationUpdates() {
        if (locationListener != null) return

        locationListener = LocationListener { location ->
            _currentCoordinates.value = location.latitude to location.longitude
        }

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                600000L,
                100f,
                locationListener!!
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }


    private fun stopLocationUpdates() {
        locationListener?.let {
            locationManager.removeUpdates(it)
        }
        locationListener = null
        _currentCoordinates.value = null
    }


    fun stopPeriodicPermissionCheck() {
        periodicCheckJob?.cancel()
        stopLocationUpdates()
    }
}



