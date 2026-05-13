package com.example.iride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iride.location.LocationData
import com.example.iride.location.LocationRepository
import com.example.iride.repository.api.RideRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RideViewModel(
    val rideRepository: RideRepository,
    val locationRepository: LocationRepository
) : ViewModel() {

    private val _rideId = MutableStateFlow("")
    val rideId: StateFlow<String> = _rideId.asStateFlow()


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

    fun fetchLocation() {
        viewModelScope.launch {
            if (locationRepository!=null){
                print("Location repo init successfully")
            }
            val location = locationRepository.getCurrentLocation()
            location?.let {

                _lastLocation.value = it
            }
        }
    }
}