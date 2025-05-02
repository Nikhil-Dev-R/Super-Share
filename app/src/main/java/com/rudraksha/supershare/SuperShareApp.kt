package com.rudraksha.supershare

import android.app.Application
import com.rudraksha.supershare.core.data.di.dataModule
import com.rudraksha.supershare.core.domain.di.domainModule
import com.rudraksha.supershare.core.utils.di.utilsModule
import com.rudraksha.supershare.core.viewmodel.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SuperShareApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SuperShareApp)
            modules(
                domainModule,
                dataModule,
                utilsModule,
                viewModelModule
            )
        }
    }
}