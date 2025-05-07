package com.rudraksha.supershare.core.domain.di

import android.content.Context
import com.rudraksha.supershare.core.domain.repository.PickerRepository
import com.rudraksha.supershare.core.domain.repository.PickerRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import com.rudraksha.supershare.core.domain.repository.DeviceDiscoveryRepository
import com.rudraksha.supershare.core.domain.repository.NearbyDeviceDiscoveryRepository

val domainModule: Module = module {
    // Provide the PickerRepository implementation
    single<PickerRepository> {
        PickerRepositoryImpl(androidContext()) 
    }

    single<DeviceDiscoveryRepository> {
        NearbyDeviceDiscoveryRepository(get())
    }
}

