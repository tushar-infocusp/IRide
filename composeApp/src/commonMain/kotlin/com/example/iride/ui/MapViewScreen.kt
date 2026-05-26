package com.example.iride.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.ic_end_point
import com.example.iride.generated.resources.ic_location
import com.example.iride.generated.resources.ic_start_point
import com.example.iride.theme.deepGreen
import com.example.iride.theme.emeraldGreen
import com.example.iride.theme.paleGreen
import com.example.iride.theme.primaryBackground
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
    initialSearchType: SearchType = SearchType.PICKUP,
    onBack: (() -> Unit)? = null,
) {
    val selectedPlace by locationViewModel.selectedPlace.collectAsState()
    val routeInfo by locationViewModel.routeInfo.collectAsState()
    val currentLocation by locationViewModel.currentLocation.collectAsState()
    val searchResults by locationViewModel.searchResults.collectAsState()

    val persistedPickup by locationViewModel.pickupAddress.collectAsState()
    val persistedDropoff by locationViewModel.dropoffAddress.collectAsState()

    var pickupPosition by remember { mutableStateOf<Position?>(persistedPickup?.let { Position(it.lon, it.lat) }) }
    var dropoffPosition by remember { mutableStateOf<Position?>(persistedDropoff?.let { Position(it.lon, it.lat) }) }

    var pickupQuery by remember { mutableStateOf(persistedPickup?.displayName ?: "") }
    var dropoffQuery by remember { mutableStateOf(persistedDropoff?.displayName ?: "") }
    
    var activeSearch by remember { mutableStateOf(initialSearchType) }
    var lastReverseGeocodeTarget by remember { mutableStateOf<SearchType?>(null) }

    val startIcon = image(painterResource(Res.drawable.ic_start_point))
    val endIcon = image(painterResource(Res.drawable.ic_end_point))
    val currentIcon = image(painterResource(Res.drawable.ic_location))

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
            if (pickupPosition == null && persistedPickup == null) {
                cameraState.position = CameraPosition(target = it, zoom = 14.0)
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
                locationViewModel.setPickupAddress(place)
            } else if (lastReverseGeocodeTarget == SearchType.DROPOFF) {
                dropoffQuery = place.displayName
                locationViewModel.setDropoffAddress(place)
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

    Box(modifier = Modifier.fillMaxSize().background(primaryBackground)) {
        // --- Map ---
        MaplibreMap(
            modifier = Modifier.fillMaxSize(),
            baseStyle = OSM_STYLE,
            cameraState = cameraState,
            onMapClick = { position, _ ->
                if (activeSearch == SearchType.PICKUP) {
                    pickupPosition = position
                    pickupQuery = "Loading address..."
                } else {
                    dropoffPosition = position
                    dropoffQuery = "Loading address..."
                }
                
                lastReverseGeocodeTarget = activeSearch
                locationViewModel.reverseGeocode(position.latitude, position.longitude)
                ClickResult.Consume
            }
        ) {
            // 🔵 Route Line
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
                    color = const(Color(0xFF4285F4)),
                    width = const(5.dp)
                )
            }

            // 📍 Markers
            pickupPosition?.let { pos ->
                val source = rememberGeoJsonSource(data = GeoJsonData.Features(FeatureCollection(listOf(Feature(Point(pos), JsonObject(emptyMap()))))))
                SymbolLayer(id = "pickup-layer", source = source, iconImage = startIcon, iconSize = const(0.15f), iconAnchor = const(SymbolAnchor.Bottom))
            }
            dropoffPosition?.let { pos ->
                val source = rememberGeoJsonSource(data = GeoJsonData.Features(FeatureCollection(listOf(Feature(Point(pos), JsonObject(emptyMap()))))))
                SymbolLayer(id = "dropoff-layer", source = source, iconImage = endIcon, iconSize = const(0.15f), iconAnchor = const(SymbolAnchor.Bottom))
            }
            
            // 📍 Current Location Indicator
            currentLocation?.let { loc ->
                val source = rememberGeoJsonSource(data = GeoJsonData.Features(FeatureCollection(listOf(Feature(Point(loc), JsonObject(emptyMap()))))))
                CircleLayer(id = "center-halo", source = source, color = const(Color(0x404285F4)), radius = const(15.dp))
                SymbolLayer(
                    id = "current-location-layer",
                    source = source,
                    iconImage = currentIcon,
                    iconSize = const(0.1f),
                    iconAnchor = const(SymbolAnchor.Bottom)
                )
            }
        }

        // --- Overlay UI ---
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(modifier = Modifier.fillMaxWidth().background(Color.White).statusBarsPadding()){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onBack?.invoke() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = deepGreen)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("EcoRide", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = deepGreen)
                }
            }

            // Toggle and Search
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Pickup / Destination Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .padding(4.dp)
                ) {
                    val isPickup = activeSearch == SearchType.PICKUP
                    ToggleItem(
                        text = "Pickup",
                        icon = Icons.Default.MyLocation,
                        isSelected = isPickup,
                        modifier = Modifier.weight(1f)
                    ) { activeSearch = SearchType.PICKUP }
                    
                    ToggleItem(
                        text = "Destination",
                        icon = Icons.Default.Navigation,
                        isSelected = !isPickup,
                        modifier = Modifier.weight(1f)
                    ) { activeSearch = SearchType.DROPOFF }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Single Search Bar
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = if (activeSearch == SearchType.PICKUP) pickupQuery else dropoffQuery,
                        onValueChange = {
                            if (activeSearch == SearchType.PICKUP) {
                                pickupQuery = it
                            } else {
                                dropoffQuery = it
                            }
                            locationViewModel.search(it)
                        },
                        placeholder = { Text("Search for a location...", color = Color.Gray) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
                
                // Search Results
                if (searchResults.isNotEmpty()) {
                    Card(
                        modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                            items(searchResults) { place ->
                                ListItem(
                                    headlineContent = { Text(place.displayName, maxLines = 1, fontSize = 14.sp) },
                                    modifier = Modifier.clickable {
                                        val position = Position(place.lon, place.lat)
                                        if (activeSearch == SearchType.PICKUP) {
                                            pickupQuery = place.displayName
                                            pickupPosition = position
                                            locationViewModel.setPickupAddress(place)
                                        } else {
                                            dropoffQuery = place.displayName
                                            dropoffPosition = position
                                            locationViewModel.setDropoffAddress(place)
                                        }
                                        cameraState.position = CameraPosition(target = position, zoom = 14.0)
                                        locationViewModel.clearSearchResults()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Floating Map Controls ---
        Column(
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MapControlButton(icon = Icons.Default.Add) {
                cameraState.position = cameraState.position.copy(zoom = cameraState.position.zoom + 1)
            }
            MapControlButton(icon = Icons.Default.Remove) {
                cameraState.position = cameraState.position.copy(zoom = cameraState.position.zoom - 1)
            }
            MapControlButton(icon = Icons.Default.MyLocation, backgroundColor = deepGreen, contentColor = Color.White) {
                currentLocation?.let { cameraState.position = CameraPosition(target = it, zoom = 14.0) }
            }
        }

        // --- Bottom Sheet/Card ---
        val currentSelectedPlace = if (activeSearch == SearchType.PICKUP) persistedPickup else persistedDropoff
        if (currentSelectedPlace != null) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(paleGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = emeraldGreen, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            val parts = currentSelectedPlace.displayName.split(", ")
                            Text(parts.firstOrNull() ?: "Select location", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = deepGreen)
                            if (parts.size > 1) {
                                Text(parts.drop(1).joinToString(", "), fontSize = 13.sp, color = Color.Gray, maxLines = 1)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Button(
                        onClick = { 
                            if (activeSearch == SearchType.PICKUP) {
                                if (persistedDropoff == null) {
                                    activeSearch = SearchType.DROPOFF
                                } else {
                                    onBack?.invoke()
                                }
                            } else {
                                if (persistedPickup == null) {
                                    activeSearch = SearchType.PICKUP
                                } else {
                                    onBack?.invoke()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = deepGreen)
                    ) {
                        Text(
                            if (activeSearch == SearchType.PICKUP) {
                                if (persistedDropoff == null) "Confirm Pickup" else "Confirm & Return"
                            } else {
                                if (persistedPickup == null) "Confirm Destination" else "Confirm & Return"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToggleItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (isSelected) deepGreen else Color.Transparent)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) Color.White else Color.Gray,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MapControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Gray,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.size(40.dp).clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
        }
    }
}
