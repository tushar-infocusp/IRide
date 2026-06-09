package com.example.iride.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.ic_find
import com.example.iride.generated.resources.ic_home
import com.example.iride.generated.resources.ic_leaf
import com.example.iride.generated.resources.ic_offer_ride
import com.example.iride.generated.resources.ic_profile
import com.example.iride.ui.PublishRideScreen
import com.example.iride.viewmodel.RideViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

object HomeTab : Tab {

    @Composable
    override fun Content() {
    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 0u,
                title = "HOME",
                icon = painterResource(Res.drawable.ic_home)
            )
        }
}


object FindRideTab : Tab {

    @Composable
    override fun Content() {

    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 1u,
                title = "FIND",
                icon = painterResource(Res.drawable.ic_find)
            )
        }
}

object OfferRideTab : Tab {

    @Composable
    override fun Content() {
        val rideViewModel : RideViewModel = koinInject()
        val navigator = LocalNavigator.currentOrThrow

        PublishRideScreen(
            rideViewModel = rideViewModel
        ){ searchType ->
            navigator.push(Maps(searchType))
        }
    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 2u,
                title = "OFFER",
                icon = painterResource(Res.drawable.ic_offer_ride)
            )
        }
}

object ImpactTab : Tab {

    @Composable
    override fun Content() {

    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 3u,
                title = "IMPACT",
                icon = painterResource(Res.drawable.ic_leaf)
            )
        }
}

object ProfileTab : Tab {

    @Composable
    override fun Content() {

    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 4u,
                title = "PROFILE",
                icon = painterResource(Res.drawable.ic_profile)
            )
        }
}