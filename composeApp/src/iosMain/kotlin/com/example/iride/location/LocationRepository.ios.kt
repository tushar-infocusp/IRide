package com.example.iride.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getCurrentLocation(): LocationData? = suspendCancellableCoroutine { continuation ->
    val locationManager = CLLocationManager()
    
    val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
        override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
            val location = didUpdateLocations.lastOrNull() as? CLLocation
            if (location != null) {
                val latitude = location.coordinate.useContents { latitude }
                val longitude = location.coordinate.useContents { longitude }
                if (continuation.isActive) {
                    continuation.resume(LocationData(latitude, longitude))
                }
            } else {
                if (continuation.isActive) {
                    continuation.resume(null)
                }
            }
            manager.stopUpdatingLocation()
        }

        override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
            if (continuation.isActive) {
                continuation.resume(null)
            }
            manager.stopUpdatingLocation()
        }
    }

    locationManager.delegate = delegate
    locationManager.desiredAccuracy = kCLLocationAccuracyBest
    locationManager.requestWhenInUseAuthorization()
    locationManager.startUpdatingLocation()

    continuation.invokeOnCancellation {
        locationManager.stopUpdatingLocation()
        // Keep a reference to the delegate so it's not garbage collected
        // because CLLocationManager.delegate is a weak reference.
        val keepAlive = delegate 
    }
}