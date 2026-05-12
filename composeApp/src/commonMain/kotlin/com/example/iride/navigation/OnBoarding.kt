package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.iride.ui.OnboardingScreen

class OnBoarding : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        OnboardingScreen{
            navigator.replace(FindRide())
        }
    }
}