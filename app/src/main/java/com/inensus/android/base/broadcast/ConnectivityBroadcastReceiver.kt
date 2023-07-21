package com.inensus.android.base.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.inensus.android.util.ConnectionChecker

class ConnectivityBroadcastReceiver(private val connectionChecker: ConnectionChecker) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        connectionChecker.observeNetworkStatus()
    }

    fun observeNetworkStatusRelay() = connectionChecker.networkStatusRelay
}