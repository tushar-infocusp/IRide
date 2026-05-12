package com.example.iride.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.iride.viewmodel.LocationViewModel
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import org.koin.compose.koinInject
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.LineLayer
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

@Composable
fun MapScreen(
    locationViewModel: LocationViewModel = koinInject()
) {

    val points = remember { mutableStateListOf<Position>() }
    val selectedPlace by locationViewModel.selectedPlace.collectAsState()

    val cameraState = rememberCameraState(
        firstPosition = CameraPosition(
            target = Position(77.2090, 28.6139), // Longitude, Latitude (New Delhi)
            zoom = 10.0,
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        MaplibreMap(
            modifier = Modifier.fillMaxSize(),
            baseStyle = OSM_STYLE,
            cameraState = cameraState,
            onMapClick = { position, _ ->
                if (points.size < 2) {
                    points.add(position)
                } else {
                    points.clear()
                    points.add(position)
                }
                locationViewModel.reverseGeocode(position.latitude, position.longitude)
                ClickResult.Consume
            }
        ) {

            if (points.isNotEmpty()) {
                val markersGeoJson = remember(points.toList()) {
                    GeoJsonData.Features(
                        FeatureCollection(
                            features = points.map { Feature(geometry = Point(it), properties = JsonObject(emptyMap())) }
                        )
                    )
                }

                val markerSource = rememberGeoJsonSource(
                    data = markersGeoJson
                )

                CircleLayer(
                    id = "marker-layer",
                    source = markerSource,
                    color = const(Color.Red),
                    radius = const(6.dp)
                )
            }

            // 🔵 Line
            if (points.size == 2) {

                val lineGeoJson = remember(points.toList()) {
                    GeoJsonData.Features(
                        FeatureCollection(
                            features = listOf(Feature(geometry = LineString(points.toList()), properties = JsonObject(emptyMap())))
                        )
                    )
                }

                val lineSource = rememberGeoJsonSource(
                    data = lineGeoJson
                )

                LineLayer(
                    id = "line-layer",
                    source = lineSource,
                    color = const(Color.Blue),
                    width = const(2.dp)
                )
            }
        }

        selectedPlace?.let { place ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Selected Location", fontWeight = FontWeight.Bold)
                    Text(text = place.displayName)
                }
            }
        }
    }
}

