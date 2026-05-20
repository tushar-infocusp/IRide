package com.example.iride.permission

expect class PermissionHandler {
    suspend fun checkPermission(permission: Array<Permission>): PermissionState
    suspend fun requestPermission(permission: Array<Permission>): PermissionState
    suspend fun enableGps(): Boolean
}


expect fun providePermissionHandler(): PermissionHandler

enum class Permission {
    LOCATION,
    CAMERA,
    STORAGE,
    NOTIFICATION
}

sealed class PermissionState {
    data class PermissionDenied(val error: Exception) : PermissionState()
    object PermissionGranted : PermissionState()
    object PermissionDeniedPermanently : PermissionState()
}


