package com.example.iride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iride.data.Place
import com.example.iride.data.RouteInfo
import com.example.iride.repository.api.LocationRepository
import com.example.iride.util.LocationService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.maplibre.spatialk.geojson.Position

class LocationViewModel(
    private val repository: LocationRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace = _selectedPlace.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _routeInfo = MutableStateFlow<RouteInfo?>(null)
    val routeInfo = _routeInfo.asStateFlow()

    private val _currentLocation = MutableStateFlow<Position?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val _pickupAddress = MutableStateFlow<Place?>(null)
    val pickupAddress = _pickupAddress.asStateFlow()

    private val _dropoffAddress = MutableStateFlow<Place?>(null)
    val dropoffAddress = _dropoffAddress.asStateFlow()

    private var searchJob: Job? = null

    fun fetchCurrentLocation() {
        viewModelScope.launch {
            println("LocationViewModel: Fetching current location...")
            val location = locationService.getCurrentLocation()
            println("LocationViewModel: Received location: $location")
            _currentLocation.value = location
        }
    }

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
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(500) // Debounce to avoid hitting Nominatim rate limits (1 req/sec)
            repository.searchPlaces(query).onSuccess {
                _searchResults.value = it
            }.onFailure {
                println("Search failed for query '$query': ${it.message}")
                it.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }

    fun getRoute(startLat: Double, startLon: Double, endLat: Double, endLon: Double) {
        viewModelScope.launch {
            repository.getRoute(startLat, startLon, endLat, endLon).onSuccess {
                _routeInfo.value = it
            }.onFailure {
                it.printStackTrace()
                _routeInfo.value = null
            }
        }
    }

    fun clearRoute() {
        _routeInfo.value = null
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }

    fun setPickupAddress(place: Place?) {
        _pickupAddress.value = place
    }

    fun setDropoffAddress(place: Place?) {
        _dropoffAddress.value = place
    }
}
