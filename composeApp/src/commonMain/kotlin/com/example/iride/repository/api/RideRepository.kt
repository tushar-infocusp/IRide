package com.example.iride.repository.api

import com.example.iride.data.CreateRideResponse
import com.example.iride.generated.resources.Res

interface RideRepository {

    /**
     * Publishes a new ride offer to the repository.
     *
     * @param origin The starting point of the journey.
     * @param destination The destination point of the journey.
     * @param seats The number of available seats for passengers.
     * @param price The cost per seat for the ride.
     */
    suspend fun publishRide(origin: String, destination: String, seats: Int, price: Double, startDateTime : Long, endDateTime : Long): Result<CreateRideResponse>

}