package com.example.iride.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val displayName: String,
    val lat: Double,
    val lon: Double,
    val address: Address? = null
)

@Serializable
data class Address(
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val suburb: String? = null,
    val road: String? = null
)

@Serializable
data class NominatimResponse(
    @SerialName("display_name") val displayName: String,
    val lat: String,
    val lon: String,
    val address: Address? = null
)
