package com.example.iride.location

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val latitude: Double,
    val longitude: Double,
)
