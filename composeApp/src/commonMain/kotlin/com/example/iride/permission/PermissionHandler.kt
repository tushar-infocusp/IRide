package com.example.iride.permission

expect class PermissionHandler {
    suspend fun checkPermission(permission: Array<Permission>): Boolean
    suspend fun requestPermission(permission: Array<Permission>): Boolean
    suspend fun enableGps(): Boolean
}


expect fun providePermissionHandler(): PermissionHandler

enum class Permission {
    LOCATION,
    CAMERA,
    STORAGE,
    NOTIFICATION
}
