package com.vazh2100.geolocation.repository

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.vazh2100.geolocation.entity.GPoint
import com.vazh2100.geolocation.entity.LocationStatus.LOCATION_OFF
import com.vazh2100.geolocation.entity.LocationStatus.LOCATION_ON
import com.vazh2100.geolocation.entity.LocationStatus.PERMISSION_DENIED
import com.vazh2100.geolocation.entity.LocationStatus.UNDEFINED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * Repository for managing location-related functionality, including permission checks,
 * geolocation status, and periodic updates of the user's current coordinates.
 */
internal class LocationStateRepository(private val context: Context) {

    private companion object {
        const val CHECK_PERMISSION_TIME_PERIOD = 20_000L // 20 sec
        const val MINIMUM_DISTANCE_TO_UPDATE = 100f // 100 m
        const val UPDATE_LOCATION_PERIOD = 600_000L // 10 minutes
    }

    //
    private val locationManager by lazy { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private var locationListener: LocationListener? = null

    //
    private val _userGPoint = MutableStateFlow<GPoint?>(null)
    val userGPoint: StateFlow<GPoint?> = _userGPoint

    /** Starts a periodic job to monitor location permission and geolocation status.
     *  Updates the location status and coordinates if conditions are met.
     */
    val locationStatus = checkStatePeriodically()

    private fun checkStatePeriodically() = flow {
        while (true) {
            emit(hasPermission())
            delay(CHECK_PERMISSION_TIME_PERIOD)
        }
    }.map { hasPermission ->
        when {
            !hasPermission -> PERMISSION_DENIED
            isGPSEnabled() && hasPermission -> LOCATION_ON
            else -> LOCATION_OFF
        }
    }.distinctUntilChanged().onEach { status ->
        when (status) {
            LOCATION_ON -> startLocationUpdates()
            LOCATION_OFF, PERMISSION_DENIED, UNDEFINED -> stopLocationUpdates()
        }
    }.onCompletion {
        stopLocationUpdates()
    }.flowOn(Dispatchers.IO)

    private fun hasPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun isGPSEnabled(): Boolean = with(locationManager) {
        isProviderEnabled(LocationManager.GPS_PROVIDER) || isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /** Starts listening for location updates if permission is granted and geolocation is enabled. */
    private suspend fun startLocationUpdates() {
        if (locationListener != null) return
        val listener = LocationListener { _userGPoint.value = GPoint(it.latitude, it.longitude) }
        try {
            withContext(Dispatchers.Main) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    UPDATE_LOCATION_PERIOD,
                    MINIMUM_DISTANCE_TO_UPDATE,
                    listener
                )
            }
            locationListener = listener
        } catch (e: SecurityException) {
            Log.e("LocationUpdates", "Failed to request location updates", e)
        }
    }

    private fun stopLocationUpdates() = locationListener?.let {
        locationManager.removeUpdates(it)
        locationListener = null
    }
}
