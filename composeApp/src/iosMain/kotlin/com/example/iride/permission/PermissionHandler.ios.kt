package com.example.iride.permission

actual class PermissionHandler {
    actual suspend fun checkPermission(permission: Array<Permission>): Boolean {
        return true
    }

    actual suspend fun requestPermission(permission: Array<Permission>): Boolean {
        return true
    }

    actual suspend fun enableGps(): Boolean {
        return true
    }
}

actual fun providePermissionHandler(): PermissionHandler {
    return PermissionHandler()
}