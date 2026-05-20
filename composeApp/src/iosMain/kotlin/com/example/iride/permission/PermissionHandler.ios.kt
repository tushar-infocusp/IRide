package com.example.iride.permission

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter
import platform.darwin.NSObject
import kotlin.coroutines.resume

actual class PermissionHandler {


    actual suspend fun checkPermission(permission: Array<Permission>): Boolean {

        var allGranted = true
        permission.forEach {
            if (allGranted) {
                val granted = when (it) {

                    Permission.LOCATION -> {
                        when (CLLocationManager.authorizationStatus()) {

                            kCLAuthorizationStatusAuthorizedAlways,
                            kCLAuthorizationStatusAuthorizedWhenInUse -> {
                                true
                            }

                            kCLAuthorizationStatusDenied -> {
                                true
                            }

                            else -> {
                                false
                            }
                        }
                    }

                    Permission.CAMERA -> {

                        when (
                            AVCaptureDevice.authorizationStatusForMediaType(
                                AVMediaTypeVideo
                            )
                        ) {

                            AVAuthorizationStatusAuthorized ->
                                true

                            AVAuthorizationStatusDenied ->
                                false

                            else ->
                                false
                        }
                    }

                    else -> false
                }

                if (!granted) {
                    allGranted = granted
                }
            }
        }
        if (!allGranted) {
            allGranted = requestPermission(permission)
        }
        return allGranted

    }

    actual suspend fun requestPermission(permission: Array<Permission>): Boolean {
        var allPermissionGranted = true
        permission.forEach {
            val granted = when (it) {
                Permission.CAMERA -> {
                    suspendCancellableCoroutine { continuation ->

                        AVCaptureDevice.requestAccessForMediaType(
                            mediaType = AVMediaTypeVideo
                        ) { granted ->

                            continuation.resume(
                                if (granted)
                                    true
                                else
                                    false
                            )
                        }
                    }
                }

                Permission.LOCATION -> {
                    // handled using CLLocationManager delegate
                    requestLocationPermission()
                }

                Permission.NOTIFICATION -> {
                    // handled using CLLocationManager delegate
                    suspendCancellableCoroutine { continuation ->
                        UNUserNotificationCenter
                            .currentNotificationCenter()
                            .requestAuthorizationWithOptions(
                                UNAuthorizationOptionAlert or
                                        UNAuthorizationOptionBadge or
                                        UNAuthorizationOptionSound
                            ) { granted, _ ->
                                continuation.resume(granted)
                            }
                    }
                }


                else -> false
            }
            if (!granted) {
                allPermissionGranted = false
            }
        }
        return allPermissionGranted
    }

    actual suspend fun enableGps(): Boolean {
        // We don't need to enable gps for ios, it fetches the location
        // after permission is granted(Need to double check)
        return true
    }

    suspend fun requestLocationPermission(): Boolean = suspendCancellableCoroutine { continuation ->
        val locationManager = CLLocationManager()

        // Inline delegate implementation to capture the async callback from iOS
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(
                manager: CLLocationManager,
                didChangeAuthorizationStatus: Int
            ) {
                if (didChangeAuthorizationStatus != kCLAuthorizationStatusNotDetermined) {
                    if (continuation.isActive) {
                        val granted =
                            (didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedAlways ||
                                    didChangeAuthorizationStatus == kCLAuthorizationStatusAuthorizedWhenInUse)
                        continuation.resume(if (granted) true else false)
                    }
                    manager.delegate = null // Clear reference to prevent cycles
                }
            }
        }

        locationManager.delegate = delegate
        // Triggers the standard system prompt
        locationManager.requestWhenInUseAuthorization()
    }
}

actual fun providePermissionHandler(): PermissionHandler {
    return PermissionHandler()
}