package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.iride.data.SignInAuthManager
import com.example.iride.ui.OnboardingScreen
import org.koin.compose.koinInject

class OnBoarding : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val signInAuthManager: SignInAuthManager = koinInject()

        OnboardingScreen{
            if (signInAuthManager.isLoggedIn) {
                navigator.replace(Main())
            } else {
                navigator.replace(SignInOptions())
            }
        }
    }
}