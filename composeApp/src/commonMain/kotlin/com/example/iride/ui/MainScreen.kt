package com.example.iride.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.iride.navigation.CustomBottomBar
import com.example.iride.navigation.Maps
import com.example.iride.navigation.OfferRideTab

@Composable
fun MainScreen(){
    TabNavigator(OfferRideTab) { tabNavigator ->

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                AnimatedVisibility(
                    visible = tabNavigator.current !is Maps,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    CustomBottomBar()
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = it.calculateBottomPadding())
            ){
                CurrentTab()
            }
        }
    }
}