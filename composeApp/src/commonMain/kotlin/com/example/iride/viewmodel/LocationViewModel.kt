package com.example.iride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iride.data.Place
import com.example.iride.repository.api.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace = _selectedPlace.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun reverseGeocode(lat: Double, lon: Double) {
        viewModelScope.launch {
            repository.reverseGeocode(lat, lon).onSuccess {
                _selectedPlace.value = it
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            repository.searchPlaces(query).onSuccess {
                _searchResults.value = it
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}
