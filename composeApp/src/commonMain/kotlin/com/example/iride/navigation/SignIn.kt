package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.iride.ui.SignInScreen

class SignIn : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        SignInScreen(
            openHomeScreen = {
                navigator.push(FindRide())
            }
        ) {
            navigator.pop()
        }
    }
}