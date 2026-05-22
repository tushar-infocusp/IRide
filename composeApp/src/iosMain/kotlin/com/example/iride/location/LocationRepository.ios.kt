package com.example.iride.location

actual suspend fun getCurrentLocation(): LocationData? {
    return LocationData(
        latitude = 0.0,
        longitude = 0.0
    )
}