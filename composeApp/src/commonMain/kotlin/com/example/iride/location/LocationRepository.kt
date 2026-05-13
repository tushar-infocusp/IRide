package com.example.iride.location

interface LocationRepository {
    suspend fun getCurrentLocation(): LocationData?
}