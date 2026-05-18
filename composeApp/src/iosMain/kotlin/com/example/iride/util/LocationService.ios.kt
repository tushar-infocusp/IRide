package com.example.iride.util

import org.maplibre.spatialk.geojson.Position

actual class LocationService actual constructor() {
    actual suspend fun getCurrentLocation(): Position? {
        // iOS implementation would use CLLocationManager
        // For now, returning null to satisfy the expect/actual contract
        return null
    }
}
