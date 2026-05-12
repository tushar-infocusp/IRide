package com.example.iride.repository.api

import com.example.iride.data.Place

interface LocationRepository {
    suspend fun reverseGeocode(lat: Double, lon: Double): Result<Place>
    suspend fun searchPlaces(query: String): Result<List<Place>>
}
