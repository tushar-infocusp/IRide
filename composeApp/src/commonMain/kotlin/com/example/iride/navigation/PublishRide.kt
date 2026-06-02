package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.iride.ui.PublishRideScreen
import com.example.iride.viewmodel.RideViewModel
import org.koin.compose.koinInject

class PublishRide: Screen {

    @Composable
    override fun Content() {
        val rideViewModel : RideViewModel = koinInject()
        PublishRideScreen(
            rideViewModel = rideViewModel
        )
    }


}