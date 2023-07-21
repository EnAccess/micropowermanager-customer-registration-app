package com.inensus.android.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.jakewharton.rxrelay2.BehaviorRelay

class ConnectionChecker(context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkStatusRelay = BehaviorRelay.create<NetworkStatus>()

    var isOnline = true

    @Suppress("DEPRECATION")
    fun observeNetworkStatus() {

        val networkStatus: NetworkStatus =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                        NetworkStatus.Online
                    } ?: run {
                        NetworkStatus.Offline
                    }
                } else {
                    connectivityManager.activeNetworkInfo?.run {
                        if (isConnectedOrConnecting) {
                            NetworkStatus.Online
                        } else {
                            NetworkStatus.Offline
                        }
                    } ?: run {
                        NetworkStatus.Offline
                    }
                }

        isOnline = networkStatus == NetworkStatus.Online

        networkStatusRelay.accept(networkStatus)
    }

    sealed class NetworkStatus {
        object Online : NetworkStatus()
        object Offline : NetworkStatus()
    }
}