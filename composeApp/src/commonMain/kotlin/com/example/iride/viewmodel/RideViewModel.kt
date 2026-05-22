package com.example.iride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iride.location.LocationData
import com.example.iride.location.getCurrentLocation
import com.example.iride.repository.api.LocationRepository
import com.example.iride.repository.api.RideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RideViewModel(
    val rideRepository: RideRepository
) : ViewModel() {

    private val _rideId = MutableStateFlow("")
    val rideId: StateFlow<String> = _rideId.asStateFlow()


    // Consume this location to get the last device location
    // TODO Check the location permission for android before this(by pass by providing permission)
    private val _lastLocation = MutableStateFlow<LocationData?>(value = null)
    val lastLocation: StateFlow<LocationData?> = _lastLocation.asStateFlow()

    suspend fun publishRide(
        origin: String,
        destination: String,
        seats: Int,
        price: Double,
        startDateTime: Long,
        endDateTime: Long
    ) {

        try {
            val result = rideRepository.publishRide(
                origin = origin,
                destination = destination,
                seats = seats,
                price = price,
                startDateTime = startDateTime,
                endDateTime = endDateTime
            )


            if (result.isSuccess) {
                _rideId.emit(result.getOrThrow().rideId)
            } else {
                throw Exception(result.exceptionOrNull()?.message)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Function to fetch location once permission is granted.
    fun fetchLocation() {
        viewModelScope.launch {
            val location = getCurrentLocation()
            location?.let {
                _lastLocation.value = it
            }
        }
    }
}