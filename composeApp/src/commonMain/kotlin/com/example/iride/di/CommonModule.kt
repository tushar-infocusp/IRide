package com.example.iride.di

import com.example.iride.client.ApiClient
import com.example.iride.repository.api.LocationRepository
import com.example.iride.repository.api.RideRepository
import com.example.iride.repository.manager.LocationRepositoryImpl
import com.example.iride.repository.manager.RideRepositoryImpl
import com.example.iride.util.LocationService
import com.example.iride.viewmodel.LocationViewModel
import com.example.iride.viewmodel.RideViewModel
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
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<LocationService> { LocationService() }

    single { RideViewModel(get()) }
    single { LocationViewModel(get(), get()) }
}