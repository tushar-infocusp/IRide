package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.iride.ui.FindRideScreen
import com.example.iride.viewmodel.RideViewModel
import org.koin.compose.koinInject

class FindRide: Screen {

    @Composable
    override fun Content() {
        val rideViewModel : RideViewModel = koinInject()
        FindRideScreen(
            rideViewModel = rideViewModel
        )
    }


}