package com.example.iride.util

import android.annotation.SuppressLint
import android.location.Location
import com.example.iride.data.AppContextHolder
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import org.maplibre.spatialk.geojson.Position
import kotlin.coroutines.resume

actual class LocationService actual constructor() {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(AppContextHolder.context)

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): Position? = suspendCancellableCoroutine { continuation ->
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                continuation.resume(Position(location.longitude, location.latitude))
            } else {
                continuation.resume(null)
            }
        }.addOnFailureListener {
            continuation.resume(null)
        }

        continuation.invokeOnCancellation {
            cancellationTokenSource.cancel()
        }
    }
}
