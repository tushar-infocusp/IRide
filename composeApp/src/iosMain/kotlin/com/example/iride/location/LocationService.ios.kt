package com.example.iride.location

import com.example.iride.permission.PermissionHandler
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


actual class LocationService :
    NSObject(),
    CLLocationManagerDelegateProtocol {

    private val locationManager = CLLocationManager()

    private var continuation:
            Continuation<LocationData?>? = null

    init {
        locationManager.delegate = this
    }

    actual suspend fun getCurrentLocation(): LocationData? =
        suspendCancellableCoroutine { cont ->

            continuation = cont

            cont.invokeOnCancellation {
                locationManager.stopUpdatingLocation()
                continuation = null
            }

            locationManager.requestWhenInUseAuthorization()
            locationManager.startUpdatingLocation()
        }

    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(
        manager: CLLocationManager,
        didUpdateLocations: List<*>
    ) {

        val location =
            didUpdateLocations.lastOrNull() as? CLLocation

        var fetchedLocation = false
        continuation?.resume(
            location?.coordinate?.useContents {
                println("Latitude : $latitude Longitude : $longitude")
                fetchedLocation =  true
                LocationData(latitude, longitude)
            }
        )

        continuation = null
        if (fetchedLocation)
            locationManager.stopUpdatingLocation()
    }

    override fun locationManager(
        manager: CLLocationManager,
        didFailWithError: NSError
    ) {
        didFailWithError.toString().let {
            println("Error: $it")
        }
        continuation?.resume(null)
        continuation = null
    }
}

actual fun provideLocationService(): LocationService {
    return LocationService()
}