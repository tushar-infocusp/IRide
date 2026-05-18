package com.example.iride.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.ic_end_point
import com.example.iride.generated.resources.ic_start_point
import com.example.iride.viewmodel.LocationViewModel
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.IconRotationAlignment
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.LineLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.LineString
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position

val OSM_STYLE = BaseStyle.Json {
    put("version", 8)
    putJsonObject("sources") {
        putJsonObject("osm") {
            put("type", "raster")
            putJsonArray("tiles") {
                add("https://tile.openstreetmap.org/{z}/{x}/{y}.png")
            }
            put("tileSize", 256)
            put("attribution", "&copy; OpenStreetMap contributors")
        }
    }
    putJsonArray("layers") {
        addJsonObject {
            put("id", "osm")
            put("type", "raster")
            put("source", "osm")
        }
    }
}

enum class SearchType { PICKUP, DROPOFF }

@Composable
fun MapScreen(
    locationViewModel: LocationViewModel = koinInject(),
) {
    val selectedPlace by locationViewModel.selectedPlace.collectAsState()
    val routeInfo by locationViewModel.routeInfo.collectAsState()
    val currentLocation by locationViewModel.currentLocation.collectAsState()
    val searchResults by locationViewModel.searchResults.collectAsState()

    var pickupPosition by remember { mutableStateOf<Position?>(null) }
    var dropoffPosition by remember { mutableStateOf<Position?>(null) }

    var pickupQuery by remember { mutableStateOf("") }
    var dropoffQuery by remember { mutableStateOf("") }
    
    var activeSearch by remember { mutableStateOf<SearchType?>(null) }
    var lastReverseGeocodeTarget by remember { mutableStateOf<SearchType?>(null) }

    val startIcon = image(painterResource(Res.drawable.ic_start_point))
    val endIcon = image(painterResource(Res.drawable.ic_end_point))

    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(
            target = Position(77.2090, 28.6139), // Longitude, Latitude (New Delhi)
            zoom = 10.0,
        ),
    )

    LaunchedEffect(Unit) {
        locationViewModel.fetchCurrentLocation()
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraState.position = CameraPosition(target = it, zoom = 14.0)
            if (pickupPosition == null) {
                pickupPosition = it
                lastReverseGeocodeTarget = SearchType.PICKUP
                locationViewModel.reverseGeocode(it.latitude, it.longitude)
            }
        }
    }

    LaunchedEffect(selectedPlace) {
        selectedPlace?.let { place ->
            if (lastReverseGeocodeTarget == SearchType.PICKUP) {
                pickupQuery = place.displayName
            } else if (lastReverseGeocodeTarget == SearchType.DROPOFF) {
                dropoffQuery = place.displayName
            }
        }
    }

    LaunchedEffect(pickupPosition, dropoffPosition) {
        if ((pickupPosition != null) && (dropoffPosition != null)) {
            locationViewModel.getRoute(
                startLat = pickupPosition!!.latitude,
                startLon = pickupPosition!!.longitude,
                endLat = dropoffPosition!!.latitude,
                endLon = dropoffPosition!!.longitude
            )
        } else {
            locationViewModel.clearRoute()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        MaplibreMap(
            modifier = Modifier.fillMaxSize(),
            baseStyle = OSM_STYLE,
            cameraState = cameraState,
            onMapClick = { position, _ ->
                // If a specific field is active, change that.
                // Otherwise, default to changing pickup if it's closer/empty, or dropoff.
                val target = activeSearch ?: if (pickupPosition == null) SearchType.PICKUP else SearchType.DROPOFF
                
                if (target == SearchType.PICKUP) {
                    pickupPosition = position
                    pickupQuery = "Loading address..."
                } else {
                    dropoffPosition = position
                    dropoffQuery = "Loading address..."
                }
                
                lastReverseGeocodeTarget = target
                locationViewModel.reverseGeocode(position.latitude, position.longitude)
                ClickResult.Consume
            }
        ) {
            // 🔵 Shortest Path Line
            routeInfo?.let { route ->
                val lineGeoJson = remember(route.points) {
                    GeoJsonData.Features(
                        FeatureCollection(
                            features = listOf(
                                Feature(
                                    geometry = LineString(route.points),
                                    properties = JsonObject(emptyMap())
                                )
                            )
                        )
                    )
                }
                val lineSource = rememberGeoJsonSource(data = lineGeoJson)
                LineLayer(
                    id = "route-layer",
                    source = lineSource,
                    color = const(Color.Blue),
                    width = const(4.dp)
                )
            }

            // 📍 Current Location Marker
            currentLocation?.let { loc ->
                val currentLocGeoJson = remember(loc) {
                    GeoJsonData.Features(
                        FeatureCollection(
                            features = listOf(Feature(geometry = Point(loc), properties = JsonObject(emptyMap())))
                        )
                    )
                }
                val currentLocSource = rememberGeoJsonSource(data = currentLocGeoJson)
                CircleLayer(
                    id = "current-location-halo",
                    source = currentLocSource,
                    color = const(Color(0x404285F4)),
                    radius = const(12.dp)
                )
                CircleLayer(
                    id = "current-location-dot",
                    source = currentLocSource,
                    color = const(Color(0xFF4285F4)),
                    radius = const(6.dp),
                    strokeColor = const(Color.White),
                    strokeWidth = const(2.dp)
                )
            }

            // 📍 Start Point Marker
            pickupPosition?.let { pos ->
                val geoJson = remember(pos) {
                    GeoJsonData.Features(
                        FeatureCollection(
                            features = listOf(Feature(geometry = Point(pos), properties = JsonObject(emptyMap())))
                        )
                    )
                }
                val source = rememberGeoJsonSource(data = geoJson)
                SymbolLayer(
                    id = "pickup-layer",
                    source = source,
                    iconImage = startIcon,
                    iconSize = const(0.15f), // Adjusted size as DP in XML might be large
                    iconAnchor = const(SymbolAnchor.Bottom)
                )
            }

            // 🏁 End Point Marker
            dropoffPosition?.let { pos ->
                val geoJson = remember(pos) {
                    GeoJsonData.Features(
                        FeatureCollection(
                            features = listOf(Feature(geometry = Point(pos), properties = JsonObject(emptyMap())))
                        )
                    )
                }
                val source = rememberGeoJsonSource(data = geoJson)
                SymbolLayer(
                    id = "dropoff-layer",
                    source = source,
                    iconImage = endIcon,
                    iconSize = const(0.15f),
                    iconAnchor = const(SymbolAnchor.Bottom)
                )
            }
        }

        // 🔍 Search Bars at Top
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Start / Pickup Search
                    TextField(
                        value = pickupQuery,
                        onValueChange = {
                            pickupQuery = it
                            activeSearch = SearchType.PICKUP
                            locationViewModel.search(it)
                        },
                        placeholder = { Text("Start location", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (it.isFocused) activeSearch = SearchType.PICKUP },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                        trailingIcon = {
                            if (pickupQuery.isNotEmpty()) {
                                IconButton(onClick = { 
                                    pickupQuery = ""
                                    pickupPosition = null
                                    locationViewModel.clearSearchResults()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp), thickness = 0.5.dp, color = Color.LightGray)

                    // End / Destination Search
                    TextField(
                        value = dropoffQuery,
                        onValueChange = {
                            dropoffQuery = it
                            activeSearch = SearchType.DROPOFF
                            locationViewModel.search(it)
                        },
                        placeholder = { Text("Where to?", fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { if (it.isFocused) activeSearch = SearchType.DROPOFF },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Red) },
                        trailingIcon = {
                            if (dropoffQuery.isNotEmpty()) {
                                IconButton(onClick = { 
                                    dropoffQuery = ""
                                    dropoffPosition = null
                                    locationViewModel.clearSearchResults()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }

            // 📍 Search Results List
            if (activeSearch != null && searchResults.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    LazyColumn(modifier = Modifier.height(200.dp)) {
                        items(searchResults) { place ->
                            ListItem(
                                headlineContent = { Text(place.displayName, maxLines = 1, fontSize = 14.sp) },
                                modifier = Modifier.clickable {
                                    val position = Position(place.lon, place.lat)
                                    if (activeSearch == SearchType.PICKUP) {
                                        pickupQuery = place.displayName
                                        pickupPosition = position
                                    } else {
                                        dropoffQuery = place.displayName
                                        dropoffPosition = position
                                    }
                                    cameraState.position = CameraPosition(target = position, zoom = 14.0)
                                    locationViewModel.clearSearchResults()
                                    activeSearch = null
                                }
                            )
                        }
                    }
                }
            }
        }

        // 📊 Route Info & Selected Place
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            routeInfo?.let { route ->
                Card(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Route Details", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Text(text = "Distance: ${formatDistance(route.distance)}", fontSize = 14.sp)
                        Text(text = "Estimated Time: ${formatDuration(route.duration)}", fontSize = 14.sp)
                    }
                }
            }

            selectedPlace?.let { place ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Current Selection", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Text(text = place.displayName, fontSize = 13.sp, maxLines = 2)
                    }
                }
            }
        }
    }
}

fun formatDistance(meters: Double): String {
    return if (meters >= 1000) {
        val km = (meters / 100).toInt() / 10.0
        "$km km"
    } else {
        "${meters.toInt()} m"
    }
}

fun formatDuration(seconds: Double): String {
    val minutes = (seconds / 60).toInt()
    return if (minutes >= 60) {
        "${minutes / 60} hr ${minutes % 60} min"
    } else {
        "$minutes min"
    }
}
