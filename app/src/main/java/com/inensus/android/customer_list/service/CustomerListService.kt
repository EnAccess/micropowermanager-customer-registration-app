package com.inensus.android.customer_list.service

import com.inensus.android.customer_list.model.AddCustomerResponse
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.customer_list.model.GetCustomerListResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface CustomerListService {
    @GET
    fun getCustomerList(
        @Url url: String,
    ): Single<GetCustomerListResponse>

    @POST
    fun addCustomer(
        @Url url: String,
        @Body customer: Customer,
    ): Observable<AddCustomerResponse>
}
