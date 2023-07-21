package com.inensus.android.add_customer.di

import com.inensus.android.add_customer.service.AddCustomerRepository
import com.inensus.android.add_customer.service.AddCustomerService
import com.inensus.android.add_customer.viewmodel.AddCustomerViewModel
import com.inensus.android.network.qualifiers.AuthQualifiers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object AddCustomerModule {

    fun create() = module {
        viewModel { AddCustomerViewModel(get(), get()) }
    } + createAddCustomerNetworkModule()

    private fun createAddCustomerNetworkModule() = module {
        single { provideAddCustomerService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }

        single { AddCustomerRepository(get(), get(), get()) }
    }

    private fun provideAddCustomerService(retrofitClient: Retrofit) = retrofitClient.create(AddCustomerService::class.java)
}