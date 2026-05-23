package com.example.iride.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.iride.data.AppContextHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AndroidConnectivityMonitor : ConnectivityMonitor {
    lateinit var cm: ConnectivityManager
    private val _status = MutableStateFlow(ConnectivityStatus.Unavailable)
    override val status: StateFlow<ConnectivityStatus>
        get() = _status

    var callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            update()
        }

        override fun onLost(network: Network) {
            update()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            update()
        }

    }


    override fun start() {
        if (!::cm.isInitialized) {
            val context = AppContextHolder.context
            cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        val req = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(req, callback)
    }

    override fun stop() {
        runCatching {
            cm.unregisterNetworkCallback(callback)
        }
    }

    fun update() {
        val active = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(active)
        val online =
            caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        _status.value = if (online) {
            ConnectivityStatus.Online
        } else {
            ConnectivityStatus.Offline
        }
    }
}

actual class ConnectivityMonitorFactory {
    actual fun create(): ConnectivityMonitor {
        return AndroidConnectivityMonitor()
    }
}