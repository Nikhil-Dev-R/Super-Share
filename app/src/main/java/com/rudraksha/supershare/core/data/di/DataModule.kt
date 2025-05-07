package com.rudraksha.supershare.core.data.di

import com.rudraksha.supershare.core.data.datastore.DeviceNameDataStore
import com.rudraksha.supershare.core.data.datastore.SettingsDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule: Module = module {
    single { SettingsDataStore(androidContext()) }
    single { DeviceNameDataStore(androidContext()) }
}