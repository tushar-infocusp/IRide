package com.example.iride.di

import com.example.iride.data.FirebaseOTPAuthManager
import com.example.iride.data.OTPAuthManager
import com.example.iride.location.LocationService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidModule = module {

    single {
        LocationService()
    }

    single { androidContext() }

    single<OTPAuthManager> {
        FirebaseOTPAuthManager()
    }

}