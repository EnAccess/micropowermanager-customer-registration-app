package com.inensus.android.network.interceptor

import android.content.Context
import android.content.Intent
import com.inensus.android.base.broadcast.SessionExpireBroadcastReceiver.Companion.SESSION_EXPIRE_INTENT_ACTION
import com.inensus.android.util.SharedPreferenceWrapper
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
    private val context: Context,
    private val sharedPreferenceWrapper: SharedPreferenceWrapper,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain
                .request()
                .newBuilder()
                .addHeader(HEADER_AUTHORIZATION, BEARER + sharedPreferenceWrapper.accessToken)
                .build()

        val response = chain.proceed(request)

        if (response.code == 401) {
            context.sendBroadcast(Intent(SESSION_EXPIRE_INTENT_ACTION))
        }

        return response
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer "
    }
}
