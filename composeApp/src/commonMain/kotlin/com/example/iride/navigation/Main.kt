package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.iride.ui.MainScreen

class Main : Screen{
    @Composable
    override fun Content() {
        MainScreen()
    }

}