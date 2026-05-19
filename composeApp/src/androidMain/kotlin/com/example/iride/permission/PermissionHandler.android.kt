package com.example.iride.permission

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.IntentSenderRequest
import androidx.core.content.ContextCompat
import com.example.iride.data.AppContextHolder
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class PermissionHandler {

    // A simple interface to communicate with the current active Activity
    interface ActivityLauncherProvider {
        fun launchPermissionRequest(manifestPermission: Array<String>, onResult: (Boolean) -> Unit)
        fun launchActivityResult(request: IntentSenderRequest, onResult: (Boolean) -> Unit)
    }

    // Temporary holder for the current active UI launcher (null when app is in background)
    var launcherProvider: ActivityLauncherProvider? = null

    actual suspend fun checkPermission(permission: Array<Permission>): Boolean {
        val context = AppContextHolder.context
        val requiredPermissions = mapToAndroidPermissions(permission)
        var granted = hasPermissions(context, requiredPermissions)

        if (!granted) {
            granted = requestPermission(permission)
        }
        return granted
    }

    private fun mapToAndroidPermissions(permissions: Array<Permission>): Array<String> {
        val requiredPermissions = mutableListOf<String>()
        permissions.forEach { permission ->
            val requiredPermission = when (permission) {
                Permission.LOCATION -> arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )

                Permission.CAMERA -> arrayOf(Manifest.permission.CAMERA)
                Permission.STORAGE -> arrayOf(
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                )

                Permission.NOTIFICATION -> arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            }
            requiredPermissions.addAll(requiredPermission.toList())
        }
        return requiredPermissions.toTypedArray()
    }


    actual suspend fun requestPermission(permission: Array<Permission>): Boolean {
        return suspendCancellableCoroutine { continuation ->
            launcherProvider?.launchPermissionRequest(
                mapToAndroidPermissions(permission)
            ) { granted ->
                if (continuation.isActive) {
                    continuation.resume(granted)
                }
            }
        }
    }

    actual suspend fun enableGps(): Boolean {
        val granted = checkPermission(arrayOf(Permission.LOCATION))
        return if (granted) {
            suspendCancellableCoroutine { continuation ->
                checkLocationEnabled(context = AppContextHolder.context, {
                    if (continuation.isActive) {
                        continuation.resume(true)
                    }
                }, { intentSenderRequest ->
                    launcherProvider?.launchActivityResult(intentSenderRequest) {
                        if (continuation.isActive) {
                            continuation.resume(it)
                        }
                    }
                })
            }
        } else {
            false
        }
    }

    fun checkLocationEnabled(
        context: Context,
        onEnabled: () -> Unit,
        onDisabled: (IntentSenderRequest) -> Unit
    ) {

        val locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000
            ).build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client =
            LocationServices.getSettingsClient(context)

        val task =
            client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            onEnabled()
        }

        task.addOnFailureListener { exception ->

            if (exception is ResolvableApiException) {

                val intentSenderRequest =
                    IntentSenderRequest.Builder(
                        exception.resolution
                    ).build()

                onDisabled(intentSenderRequest)
            }
        }
    }

    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}


actual fun providePermissionHandler(): PermissionHandler {
    return PermissionHandler()
}