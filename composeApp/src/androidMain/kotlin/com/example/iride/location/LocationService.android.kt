package com.example.iride.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.iride.data.AppContextHolder
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class LocationService {

    @OptIn(InternalCoroutinesApi::class)
    actual suspend fun getCurrentLocation(): LocationData? =
        suspendCancellableCoroutine { continuation ->
            val context = AppContextHolder.context
            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                continuation.resumeWithException(Exception("No Permission granted"))


            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        if (continuation.isActive) {
                            continuation.resume(
                                LocationData(
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            )
                        }
                    }
                }.addOnFailureListener { failure ->
                    continuation.resumeWithException(failure)
                }
            }
        }
}
