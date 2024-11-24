package com.vazh2100.geoeventapp.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NetworkStateRepository(context: Context) {

    private val connectivityManager = getSystemService(context, ConnectivityManager::class.java)

    private val _networkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.UNKNOWN)
    val networkStatus: StateFlow<NetworkStatus> = _networkStatus

    init {
        observeNetworkChanges()
    }


    private fun getNetworkStatus(): NetworkStatus {
        val networkCapabilities =
            connectivityManager?.activeNetwork.let { connectivityManager?.getNetworkCapabilities(it) }
        return when {
            networkCapabilities == null -> NetworkStatus.DISCONNECTED
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkStatus.CONNECTED
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkStatus.CONNECTED
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkStatus.CONNECTED
            else -> NetworkStatus.DISCONNECTED
        }
    }


    private fun observeNetworkChanges() {
        _networkStatus.value = getNetworkStatus()
        connectivityManager?.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _networkStatus.value = NetworkStatus.CONNECTED
            }

            override fun onLost(network: Network) {
                _networkStatus.value = NetworkStatus.DISCONNECTED
            }

            override fun onUnavailable() {
                _networkStatus.value = NetworkStatus.DISCONNECTED
            }

            override fun onCapabilitiesChanged(
                network: Network, capabilities: NetworkCapabilities
            ) {
                val isInternetAvailable =
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                if (isInternetAvailable) {
                    _networkStatus.value = NetworkStatus.CONNECTED
                } else {
                    _networkStatus.value = NetworkStatus.DISCONNECTED
                }
            }
        })
    }
}

