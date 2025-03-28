package network.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import network.entity.NetworkStatus

internal class NetworkStateRepository(private val context: Context) {

    private companion object {
        const val BACKGROUND_TIMEOUT = 10000L // 10 seconds
    }

    //
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // если сети при подписки нет, то Callback ничего не вернёт, поэтому значение по умолчанию Disconnect
    // если сеть при подписке есть, то Callback при подписке вернёт Connected
    val networkState = callbackFlow {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) ?: return@callbackFlow

        val callBack = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) = trySend(NetworkStatus.CONNECTED).let { }
            override fun onLost(network: Network) = trySend(NetworkStatus.DISCONNECTED).let { }

            @RequiresApi(Build.VERSION_CODES.P)
            override fun onCapabilitiesChanged(
                network: Network,
                capabilities: NetworkCapabilities
            ) {
                val isCellular = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                val cellularHasData = try {
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
                } catch (_: Throwable) {
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                }
                val isWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                val wifiHasData = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                val state = when {
                    isCellular && cellularHasData -> NetworkStatus.CONNECTED
                    isWifi && wifiHasData -> NetworkStatus.CONNECTED
                    !isCellular && !isWifi -> NetworkStatus.CONNECTED
                    else -> NetworkStatus.DISCONNECTED
                }
                trySend(state)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callBack)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callBack)
        }
    }.stateIn(repositoryScope, SharingStarted.WhileSubscribed(BACKGROUND_TIMEOUT, 0), NetworkStatus.DISCONNECTED)
}
