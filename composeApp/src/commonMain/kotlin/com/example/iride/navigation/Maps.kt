package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.iride.ui.MapScreen
import com.example.iride.ui.SearchType

class Maps(private val initialSearchType: SearchType = SearchType.PICKUP) : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(index = 0u, title = "Maps")

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        MapScreen(
            initialSearchType = initialSearchType,
            onBack = { navigator.pop() }
        )
    }

}