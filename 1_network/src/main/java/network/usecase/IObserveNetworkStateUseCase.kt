package network.usecase

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import network.entity.NetworkStatus
import network.entity.NetworkStatus.CONNECTED
import network.entity.NetworkStatus.DISCONNECTED

interface IObserveNetworkStateUseCase {
    operator fun invoke(): Flow<NetworkStatus>
}

internal class ObserveNetworkStateUseCase(val context: Context) : IObserveNetworkStateUseCase {

    override operator fun invoke() = callbackFlow {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) ?: return@callbackFlow
        val callBack = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) = trySend(CONNECTED).let { }
            override fun onLost(network: Network) = trySend(DISCONNECTED).let { }

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
                    isCellular && cellularHasData -> CONNECTED
                    isWifi && wifiHasData -> CONNECTED
                    !isCellular && !isWifi -> CONNECTED
                    else -> DISCONNECTED
                }
                trySend(state)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callBack)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callBack)
        }
    }.conflate().flowOn(Dispatchers.IO)
}
