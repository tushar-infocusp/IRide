package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.iride.ui.PhoneVerificationScreen

class PhoneVerification : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        PhoneVerificationScreen(
            onVerificationSuccess = {
                navigator.push(PublishRide())
            },
            onBackClick = {
                navigator.pop()
            }
        )
    }
}