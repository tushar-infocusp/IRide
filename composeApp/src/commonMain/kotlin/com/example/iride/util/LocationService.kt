package com.example.iride.util

import org.maplibre.spatialk.geojson.Position

expect class LocationService() {
    suspend fun getCurrentLocation(): Position?
}
