package com.example.iride.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.example.iride.navigation.CustomBottomBar
import com.example.iride.navigation.OfferRideTab

@Composable
fun MainScreen(){
    TabNavigator(OfferRideTab) {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {

                CustomBottomBar()

            }
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = it.calculateBottomPadding())
            ){
                CurrentTab()
            }
        }
    }
}