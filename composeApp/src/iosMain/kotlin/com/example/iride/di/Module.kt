package com.example.iride.di

import com.example.iride.data.FirebaseEmailAuthManager
import com.example.iride.data.FirebaseOTPAuthManager
import com.example.iride.data.OTPAuthManager
import com.example.iride.data.SignInAuthManager
import com.example.iride.data.local.AppDatabase
import com.example.iride.data.local.getDatabaseBuilder
import com.example.iride.data.local.getRoomDatabase
import com.example.iride.location.LocationService
import com.example.iride.viewmodel.RideViewModel
import org.koin.dsl.module

val iosModule = module {

    single { getDatabaseBuilder() }
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().rideDao() }

    single<OTPAuthManager> {
        FirebaseOTPAuthManager()
    }

    single<SignInAuthManager> {
        FirebaseEmailAuthManager()
    }

    single {
        RideViewModel(
            get(), get(), get()
        )
    }


}
