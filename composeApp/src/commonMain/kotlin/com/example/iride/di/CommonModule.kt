package com.example.iride.di

import com.example.iride.client.ApiClient
import com.example.iride.location.LocationRepository
import com.example.iride.location.LocationRepositoryImpl
import com.example.iride.location.LocationService
import com.example.iride.repository.api.RideRepository
import com.example.iride.repository.manager.RideRepositoryImpl
import com.example.iride.viewmodel.RideViewModel
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(
    platformModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        appDeclaration()
        modules(
            platformModules +
                    clientModule +
                    appModule
        )
    }
}

val clientModule = module {
    single { ApiClient.client }
}

val appModule = module {
    single<RideRepository> { RideRepositoryImpl(get()) }

    single<LocationRepository> {
        LocationRepositoryImpl(get())
    }
    // ⚠️ CRITICAL FIX: ViewModels/ScreenModels MUST be a factory.
    // If you use 'single', the state will never reset when you leave and return to the screen.
    factory { RideViewModel(get(), get()) }
}