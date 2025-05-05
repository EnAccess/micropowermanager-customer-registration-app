package com.inensus.android

import android.app.Application
import com.inensus.android.add_customer.di.AddCustomerModule
import com.inensus.android.base.di.ApplicationModule
import com.inensus.android.customer_list.di.CustomerListModule
import com.inensus.android.login.di.LoginModule
import com.inensus.android.network.di.AuthNetworkModule
import com.inensus.android.network.di.CoreNetworkModule
import com.inensus.android.network.di.NoAuthNetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class InensusApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@InensusApplication)
            modules(
                    listOf(
                            ApplicationModule.create(),
                            CoreNetworkModule.create(),
                            AuthNetworkModule.create(),
                            NoAuthNetworkModule.create(),
                            *LoginModule.create().toTypedArray(),
                            *CustomerListModule.create().toTypedArray(),
                            *AddCustomerModule.create().toTypedArray()
                    )
            )
        }
    }
}
