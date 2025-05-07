package com.rudraksha.supershare.core.viewmodel.di

import android.app.Application
import com.rudraksha.supershare.core.data.datastore.SettingsDataStore
import com.rudraksha.supershare.core.domain.repository.PickerRepository
import com.rudraksha.supershare.core.viewmodel.CommunicationViewModel
import com.rudraksha.supershare.core.viewmodel.DiscoveryViewModel
import com.rudraksha.supershare.core.viewmodel.HistoryViewModel
import com.rudraksha.supershare.core.viewmodel.HotspotViewModel
import com.rudraksha.supershare.core.viewmodel.PermissionsViewModel
import com.rudraksha.supershare.core.viewmodel.PickerScreenViewModel
import com.rudraksha.supershare.core.viewmodel.SettingsViewModel
import com.rudraksha.supershare.core.viewmodel.TransferViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SettingsViewModel(
            app = androidApplication(),
//            settingsDataStore = get(),
//            deviceNameDataStore = get()
        )
    }
    viewModel { DiscoveryViewModel() }
    viewModel { TransferViewModel() }
    viewModel {
        PickerScreenViewModel(
            application = androidApplication()
        )
    }

    viewModel {
        HistoryViewModel(
            application = androidApplication()
        )
    }

    viewModel {
        PermissionsViewModel()
    }

    viewModel {
        HotspotViewModel(
            application = androidApplication()
        )
    }

    viewModel {
        CommunicationViewModel(
            application = androidApplication()
        )
    }
}
