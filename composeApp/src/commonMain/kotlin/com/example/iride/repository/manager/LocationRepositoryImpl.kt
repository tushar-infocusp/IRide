package com.example.iride.repository.manager

import com.example.iride.data.NominatimResponse
import com.example.iride.data.OsrmResponse
import com.example.iride.data.Place
import com.example.iride.data.PolylineDecoder
import com.example.iride.data.RouteInfo
import com.example.iride.repository.api.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

class LocationRepositoryImpl(private val client: HttpClient) : LocationRepository {

    override suspend fun reverseGeocode(lat: Double, lon: Double): Result<Place> {
        return try {
            println("LocationRepository: Reverse geocoding $lat, $lon")
            val response: HttpResponse = client.get("https://nominatim.openstreetmap.org/reverse") {
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("format", "json")
                parameter("addressdetails", 1)
                header("User-Agent", "IRide-App-Contact-Tushar")
            }

            if (response.status.isSuccess()) {
                val data = response.body<NominatimResponse>()
                Result.success(
                    Place(
                        displayName = data.displayName,
                        lat = data.lat.toDouble(),
                        lon = data.lon.toDouble(),
                        address = data.address?.copy(
                            lat = data.lat.toDouble(),
                            lon = data.lon.toDouble()
                        )
                    )
                )
            } else {
                val errorBody = response.body<String>()
                println("LocationRepository: Reverse geocode failed with status ${response.status}: $errorBody")
                Result.failure(Exception("HTTP ${response.status}: $errorBody"))
            }
        } catch (e: Exception) {
            println("LocationRepository: Reverse geocode exception: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun searchPlaces(query: String): Result<List<Place>> {
        return try {
            println("LocationRepository: Searching places for query: '$query'")
            val response: HttpResponse = client.get("https://nominatim.openstreetmap.org/search") {
                parameter("q", query)
                parameter("format", "json")
                parameter("addressdetails", 1)
                parameter("limit", 5)
                header("User-Agent", "IRide-App-Contact-Tushar")
            }

            if (response.status.isSuccess()) {
                val data = response.body<List<NominatimResponse>>()
                println("LocationRepository: Found ${data.size} results")
                Result.success(
                    data.map {
                        Place(
                            displayName = it.displayName,
                            lat = it.lat.toDouble(),
                            lon = it.lon.toDouble(),
                            address = it.address?.copy(
                                lat = it.lat.toDouble(),
                                lon = it.lon.toDouble()
                            )
                        )
                    }
                )
            } else {
                val errorBody = response.body<String>()
                println("LocationRepository: Search failed with status ${response.status}: $errorBody")
                Result.failure(Exception("HTTP ${response.status}: $errorBody"))
            }
        } catch (e: Exception) {
            println("LocationRepository: Search exception: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getRoute(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): Result<RouteInfo> {
        return try {
            println("LocationRepository: Getting route from ($startLat, $startLon) to ($endLat, $endLon)")
            val url = "https://router.project-osrm.org/route/v1/driving/$startLon,$startLat;$endLon,$endLat"
            val response: HttpResponse = client.get(url) {
                parameter("overview", "full")
                parameter("geometries", "polyline")
                parameter("annotations", "true") 
            }

            if (response.status.isSuccess()) {
                val data = response.body<OsrmResponse>()
                if (data.code == "Ok" && data.routes.isNotEmpty()) {
                    val route = data.routes[0]
                    val decodedPoints = PolylineDecoder.decode(route.geometry)
                    println("LocationRepository: Route found. Distance: ${route.distance}, Duration: ${route.duration}")
                    Result.success(
                        RouteInfo(
                            points = decodedPoints,
                            distance = route.distance,
                            duration = route.duration
                        )
                    )
                } else {
                    println("LocationRepository: No route found: ${data.code}")
                    Result.failure(Exception("No route found: ${data.code}"))
                }
            } else {
                val errorBody = response.body<String>()
                println("LocationRepository: Route request failed with status ${response.status}: $errorBody")
                Result.failure(Exception("HTTP ${response.status}: $errorBody"))
            }
        } catch (e: Exception) {
            println("LocationRepository: Route exception: ${e.message}")
            Result.failure(e)
        }
    }
}
