package com.inensus.android.network.di

import android.content.Context
import com.inensus.android.network.interceptor.AuthorizationInterceptor
import com.inensus.android.network.model.InterceptorsModel
import com.inensus.android.network.qualifiers.AuthQualifiers
import com.inensus.android.network.qualifiers.Qualifiers
import com.inensus.android.util.SharedPreferenceWrapper
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit

object AuthNetworkModule {

    fun create(): Module = module {
        single(qualifier = AuthQualifiers.AUTH_RETROFIT) {
            get<Retrofit>(
                qualifier = Qualifiers.BASE_RETROFIT,
                parameters = { parametersOf(provideInterceptors(get(), get())) }
            )
        }
    }

    private fun provideInterceptors(
        context: Context,
        sharedPreferenceWrapper: SharedPreferenceWrapper
    ): InterceptorsModel = InterceptorsModel(
        interceptors = listOf(AuthorizationInterceptor(context, sharedPreferenceWrapper)),
        networkInterceptors = emptyList()
    )
}
