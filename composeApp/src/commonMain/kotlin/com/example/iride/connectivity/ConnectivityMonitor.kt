package com.example.iride.connectivity

import com.example.iride.location.LocationService
import kotlinx.coroutines.flow.StateFlow

/** Simple cross-platform status */
enum class ConnectivityStatus { Online, Offline, Unavailable }

/** Platform monitor contract */
interface ConnectivityMonitor {
    val status: StateFlow<ConnectivityStatus>
    fun start()
    fun stop()
}


expect fun provideConnectivityFactory(): ConnectivityMonitor
