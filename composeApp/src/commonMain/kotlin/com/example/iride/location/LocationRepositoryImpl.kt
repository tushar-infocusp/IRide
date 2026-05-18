package com.example.iride.location

class LocationRepositoryImpl(var locationService: LocationService): LocationRepository {
    override suspend fun getCurrentLocation(): LocationData? {
        return locationService.getCurrentLocation()
    }
}