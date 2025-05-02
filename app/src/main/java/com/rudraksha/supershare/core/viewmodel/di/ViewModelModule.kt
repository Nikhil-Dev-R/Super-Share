package com.rudraksha.supershare.core.viewmodel.di

import com.rudraksha.supershare.core.viewmodel.DiscoveryViewModel
import com.rudraksha.supershare.core.viewmodel.SettingsViewModel
import com.rudraksha.supershare.core.viewmodel.TransferViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // DataStore-backed ViewModel
    viewModel { SettingsViewModel(get(), androidApplication()) }
    viewModel { DiscoveryViewModel() }
    viewModel { TransferViewModel() }
//    viewModel { PickerViewModel(get(), get()) }
//    viewModel { HistoryViewModel(get()) }
}
