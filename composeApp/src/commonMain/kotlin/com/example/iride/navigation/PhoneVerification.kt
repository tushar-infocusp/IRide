package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.iride.data.OTPAuthManager
import com.example.iride.data.Platform
import com.example.iride.ui.PhoneVerificationScreen
import com.example.iride.ui.SignInScreen
import org.koin.compose.koinInject

class PhoneVerification : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        PhoneVerificationScreen(
            onVerificationSuccess = {
                navigator.pop()
            },
            onBackClick = {
                navigator.pop()
            }
        )
    }
}