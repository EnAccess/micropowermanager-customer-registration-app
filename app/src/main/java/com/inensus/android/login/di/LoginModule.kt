package com.inensus.android.login.di

import com.inensus.android.login.repository.LoginRepository
import com.inensus.android.login.service.LoginService
import com.inensus.android.login.view.LoginFormValidator
import com.inensus.android.login.viewmodel.LoginViewModel
import com.inensus.android.network.qualifiers.NoAuthQualifiers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object LoginModule {

    fun create() = module {
        single { LoginFormValidator() }
        viewModel { LoginViewModel(get(), get(), get()) }
    } + createLoginNetworkModule()

    private fun createLoginNetworkModule() = module {
        single { provideLoginService(get(qualifier = NoAuthQualifiers.NO_AUTH_RETROFIT)) }

        single { LoginRepository(get(), get()) }
    }

    private fun provideLoginService(retrofitClient: Retrofit) =
        retrofitClient.create(LoginService::class.java)
}
