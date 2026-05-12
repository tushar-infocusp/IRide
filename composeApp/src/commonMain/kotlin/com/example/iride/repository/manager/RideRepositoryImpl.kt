package com.example.iride.repository.manager

import com.example.iride.data.BASE_URL
import com.example.iride.data.CreateRideRequest
import com.example.iride.data.CreateRideResponse
import com.example.iride.repository.api.RideRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class RideRepositoryImpl(private val client: HttpClient) : RideRepository {

    override suspend fun publishRide(
        origin: String,
        destination: String,
        seats: Int,
        price: Double,
        startDateTime: Long,
        endDateTime: Long
    ) : Result<CreateRideResponse> {
        try {
            val createRideRequest = CreateRideRequest(
                origin = origin,
                destination = destination,
                seats = seats,
                price = price,
                startDateTime = startDateTime,
                endDateTime = endDateTime
            )
            val response = client.post("$BASE_URL/rides/publish") {
                setBody(createRideRequest)
            }

            val rideResponse = response.body<CreateRideResponse>()
            return Result.success(rideResponse)
        }
        catch (e : Exception){
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}