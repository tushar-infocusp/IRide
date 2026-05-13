package com.example.iride.location

expect class LocationService {
    suspend fun getCurrentLocation(): LocationData?
}

