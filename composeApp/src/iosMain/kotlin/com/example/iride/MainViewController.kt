package com.example.iride

import androidx.compose.ui.window.ComposeUIViewController
import com.example.iride.di.initKoin
import com.example.iride.di.iosModule

fun MainViewController() = ComposeUIViewController {
    initKoin(listOf(iosModule))
    App() }