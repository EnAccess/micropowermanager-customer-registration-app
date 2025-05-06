package com.inensus.android.network.di

import com.inensus.android.network.model.InterceptorsModel
import com.inensus.android.network.qualifiers.NoAuthQualifiers
import com.inensus.android.network.qualifiers.Qualifiers
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit

object NoAuthNetworkModule {

    fun create() = module {
        single(qualifier = NoAuthQualifiers.NO_AUTH_RETROFIT) {
            get<Retrofit>(
                qualifier = Qualifiers.BASE_RETROFIT,
                parameters = { parametersOf(provideInterceptors()) })
        }
    }

    private fun provideInterceptors(): InterceptorsModel = InterceptorsModel(
        interceptors = emptyList(),
        networkInterceptors = emptyList()
    )
}
