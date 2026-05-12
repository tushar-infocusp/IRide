package com.example.iride.repository.manager

import com.example.iride.data.NominatimResponse
import com.example.iride.data.Place
import com.example.iride.repository.api.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class LocationRepositoryImpl(private val client: HttpClient) : LocationRepository {

    override suspend fun reverseGeocode(lat: Double, lon: Double): Result<Place> {
        return try {
            val response = client.get("https://nominatim.openstreetmap.org/reverse") {
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("format", "json")
                parameter("addressdetails", 1)
                header("User-Agent", "IRide-App")
            }.body<NominatimResponse>()

            Result.success(
                Place(
                    displayName = response.displayName,
                    lat = response.lat.toDouble(),
                    lon = response.lon.toDouble(),
                    address = response.address
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPlaces(query: String): Result<List<Place>> {
        return try {
            val response = client.get("https://nominatim.openstreetmap.org/search") {
                parameter("q", query)
                parameter("format", "json")
                parameter("addressdetails", 1)
                parameter("limit", 5)
                header("User-Agent", "IRide-App")
            }.body<List<NominatimResponse>>()

            Result.success(
                response.map {
                    Place(
                        displayName = it.displayName,
                        lat = it.lat.toDouble(),
                        lon = it.lon.toDouble(),
                        address = it.address
                    )
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
