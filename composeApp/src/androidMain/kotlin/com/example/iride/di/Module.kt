package com.example.iride.di

import com.example.iride.data.FirebaseOTPAuthManager
import com.example.iride.data.OTPAuthManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidModule = module {

    single { androidContext() }

    single<OTPAuthManager> {
        FirebaseOTPAuthManager()
    }

}