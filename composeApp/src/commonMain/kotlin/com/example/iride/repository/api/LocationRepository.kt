package com.example.iride.repository.api

import com.example.iride.data.Place
import com.example.iride.data.RouteInfo

interface LocationRepository {
    suspend fun reverseGeocode(lat: Double, lon: Double): Result<Place>
    suspend fun searchPlaces(query: String): Result<List<Place>>
    suspend fun getRoute(startLat: Double, startLon: Double, endLat: Double, endLon: Double): Result<RouteInfo>
}
