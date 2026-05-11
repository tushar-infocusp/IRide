package com.example.iride.data

import kotlinx.serialization.Serializable

const val BASE_URL = "http://10.20.40.198:8080/api"

@Serializable
data class CreateRideRequest(
    val origin: String = "",
    val destination: String = "",
    val seats: Int = 0,
    val price: Double = 0.0,
    val startDateTime : Long = 0L,
    val endDateTime : Long = 0L,
)

@Serializable
data class CreateRideResponse(
    val rideId: String
)

@Serializable
data class Ride(
    val id: String,
    val driverId: String,
    val origin: String,
    val destination: String,
    val seats: Int,
    val price: Double,
    val status: String
)