package com.inensus.android.base.di

import com.inensus.android.base.broadcast.ConnectivityBroadcastReceiver
import com.inensus.android.base.broadcast.SessionExpireBroadcastReceiver
import com.inensus.android.database.InensusDatabase
import com.inensus.android.util.ConnectionChecker
import com.inensus.android.util.SharedPreferenceWrapper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object ApplicationModule {
    fun create() =
        module {
            single { SharedPreferenceWrapper(get()) }
            single { InensusDatabase.getDatabase(androidApplication())?.customerDao() }
            single { SessionExpireBroadcastReceiver() }
            single { ConnectivityBroadcastReceiver(get()) }
            single { ConnectionChecker(get()) }
        }
}
