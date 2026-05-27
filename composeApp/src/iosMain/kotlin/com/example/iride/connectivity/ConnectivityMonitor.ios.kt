package com.example.iride.connectivity

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.staticCFunction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import kotlinx.cinterop.*
import platform.CoreFoundation.CFRunLoopGetCurrent
import platform.CoreFoundation.kCFRunLoopDefaultMode
import platform.SystemConfiguration.*
import platform.posix.AF_INET
import platform.posix.sockaddr_in

actual class ConnectivityMonitorFactory {
    class IosConnectivityMonitor : ConnectivityMonitor {

        private val _status = MutableStateFlow(ConnectivityStatus.Unavailable)
        override val status: StateFlow<ConnectivityStatus> = _status

        @OptIn(ExperimentalForeignApi::class)
        private var reachability: SCNetworkReachabilityRef? = null

        @OptIn(ExperimentalForeignApi::class)
        private var selfRef: StableRef<IosConnectivityMonitor>? = null
        private val scope = CoroutineScope(Dispatchers.Main)

        init {
            start()
        }

        @OptIn(ExperimentalForeignApi::class)
        override fun start() {
            if (reachability != null) return

            // Monitor default route reachability (0.0.0.0)
            val ref = memScoped {
                val addr = alloc<sockaddr_in>(

                ).apply {
                    sin_len = sizeOf<sockaddr_in>().toUByte()
                    sin_family = AF_INET.convert()
                    sin_port = 0u
                    sin_addr.s_addr = 0u
                }
                SCNetworkReachabilityCreateWithAddress(null, addr.ptr.reinterpret())
            }
            reachability = ref ?: run {
                scope.launch { _status.value = ConnectivityStatus.Unavailable }
                return
            }

            // StableRef to pass 'this' through the C callback
            selfRef = StableRef.create(this)

            val callback =
                staticCFunction<SCNetworkReachabilityRef?, SCNetworkReachabilityFlags, COpaquePointer?, Unit> { _, flags, info ->
                    val instance =
                        info?.asStableRef<IosConnectivityMonitor>()?.get() ?: return@staticCFunction
                    instance.update(flags)
                }

            val context = nativeHeap.alloc<SCNetworkReachabilityContext>().apply {
                version = 0
                info = selfRef!!.asCPointer()
                retain = null
                release = null
                copyDescription = null
            }

            // Register callback and schedule on the current run loop
            if (SCNetworkReachabilitySetCallback(reachability, callback, context.ptr)) {
                val loop = CFRunLoopGetCurrent()
                if (SCNetworkReachabilityScheduleWithRunLoop(
                        reachability, loop, kCFRunLoopDefaultMode
                    )
                ) {
                    // Emit initial value
                    memScoped {
                        val flagsVar = alloc<SCNetworkReachabilityFlagsVar>()
                        if (SCNetworkReachabilityGetFlags(reachability, flagsVar.ptr)) {
                            update(flagsVar.value)
                        } else {
                            scope.launch { _status.value = ConnectivityStatus.Unavailable }
                        }
                    }
                    return
                }
            }

            // If scheduling failed, emit a one-off status
            memScoped {
                val flagsVar = alloc<SCNetworkReachabilityFlagsVar>()
                if (SCNetworkReachabilityGetFlags(reachability, flagsVar.ptr)) {
                    update(flagsVar.value)
                } else {
                    scope.launch { _status.value = ConnectivityStatus.Unavailable }
                }
            }
        }

        private fun update(flags: SCNetworkReachabilityFlags) {
            val reachable = flags.toInt() and kSCNetworkReachabilityFlagsReachable.toInt() != 0
            val needsConn =
                flags.toInt() and kSCNetworkReachabilityFlagsConnectionRequired.toInt() != 0
            val online = reachable && !needsConn
            scope.launch {
                _status.value =
                    if (online) ConnectivityStatus.Online else ConnectivityStatus.Offline
            }
        }

        @OptIn(ExperimentalForeignApi::class)
        override fun stop() {
            reachability?.let {
                val loop = CFRunLoopGetCurrent()
                SCNetworkReachabilityUnscheduleFromRunLoop(it, loop, kCFRunLoopDefaultMode)
            }
            reachability = null
            selfRef?.dispose()
            selfRef = null
        }

    }

    actual fun create(): ConnectivityMonitor {
        return IosConnectivityMonitor()
    }
}

actual fun provideConnectivityFactory(): ConnectivityMonitor{
    return ConnectivityMonitorFactory.IosConnectivityMonitor()
}