package com.inensus.android.customer_list.di

import com.inensus.android.customer_list.service.CustomerListRepository
import com.inensus.android.customer_list.service.CustomerListService
import com.inensus.android.customer_list.viewmodel.CustomerListViewModel
import com.inensus.android.network.qualifiers.AuthQualifiers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

object CustomerListModule {

    fun create() = module {
        viewModel { CustomerListViewModel(get(), get()) }
    } + createCustomerListNetworkModule()

    private fun createCustomerListNetworkModule() = module {
        single { provideCustomerListService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }

        single { CustomerListRepository(get(), get(), get(), get()) }
    }

    private fun provideCustomerListService(retrofitClient: Retrofit) = retrofitClient.create(CustomerListService::class.java)
}