package com.vazh2100.network.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.vazh2100.network.entity.NetworkStatus.CONNECTED
import com.vazh2100.network.entity.NetworkStatus.DISCONNECTED
import com.vazh2100.network.entity.NetworkStatus.UNKNOWN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Repository for monitoring network connectivity changes and managing the current network status. */
internal class NetworkStateRepository(context: Context) {
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    //
    private val _networkStatus = MutableStateFlow(UNKNOWN)
    val networkStatus = _networkStatus.asStateFlow()
    private var networkCallback: StateCallback? = null

    init {
        startObserve()
    }

    fun startObserve() {
        if (networkCallback != null) return
        networkCallback = StateCallback()
        networkCallback?.let { connectivityManager?.registerDefaultNetworkCallback(it) }
    }

    fun stopObserve() {
        networkCallback?.let {
            connectivityManager?.unregisterNetworkCallback(it)
            _networkStatus.value = UNKNOWN
        }
    }

    private inner class StateCallback : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _networkStatus.value = CONNECTED
        }

        override fun onLost(network: Network) {
            _networkStatus.value = DISCONNECTED
        }

        @RequiresApi(Build.VERSION_CODES.S)
        override fun onCapabilitiesChanged(
            network: Network,
            capabilities: NetworkCapabilities
        ) {
            val isCellular = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            val cellularHasData = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
            val isWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val wifiHasData = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            _networkStatus.value = when {
                isCellular && cellularHasData -> CONNECTED
                isWifi && wifiHasData -> CONNECTED
                !isCellular && !isWifi -> CONNECTED
                else -> DISCONNECTED
            }
        }
    }
}
