package com.example.iride.viewmodel

import androidx.lifecycle.ViewModel
import com.example.iride.repository.api.RideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RideViewModel(
    val rideRepository: RideRepository
) : ViewModel() {

    private val _rideId = MutableStateFlow("")
    val rideId : StateFlow<String> = _rideId.asStateFlow()

    suspend fun publishRide(origin : String, destination : String, seats : Int, price : Double, startDateTime : Long, endDateTime : Long){

        try {
            val result = rideRepository.publishRide(
                origin = origin,
                destination = destination,
                seats = seats,
                price = price,
                startDateTime = startDateTime,
                endDateTime = endDateTime
            )


            if(result.isSuccess){
                _rideId.emit(result.getOrThrow().rideId)
            }
            else{
                throw Exception(result.exceptionOrNull()?.message)
            }

        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }
}