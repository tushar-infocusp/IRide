package com.example.iride.di

import com.example.iride.data.FirebaseEmailAuthManager
import com.example.iride.data.FirebaseOTPAuthManager
import com.example.iride.data.OTPAuthManager
import com.example.iride.data.SignInAuthManager
import com.example.iride.location.LocationService
import com.example.iride.viewmodel.RideViewModel
import org.koin.dsl.module

val iosModule = module {

    single<OTPAuthManager> {
        FirebaseOTPAuthManager()
    }

    single<SignInAuthManager> {
        FirebaseEmailAuthManager()
    }

    single {
        LocationService()
    }

    single {
        RideViewModel(get(), get())
    }


}