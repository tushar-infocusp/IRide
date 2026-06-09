package com.example.iride.di

import com.example.iride.data.FirebaseOTPAuthManager
import com.example.iride.data.OTPAuthManager
import com.example.iride.data.local.AppDatabase
import com.example.iride.data.local.getDatabaseBuilder
import com.example.iride.data.local.getRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidModule = module {
    single { androidContext() }

    single { getDatabaseBuilder() }
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().rideDao() }

    single<OTPAuthManager> {
        FirebaseOTPAuthManager()
    }

}