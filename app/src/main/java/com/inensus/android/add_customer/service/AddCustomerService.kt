package com.inensus.android.add_customer.service

import com.inensus.android.add_customer.model.GetCityListResponse
import com.inensus.android.add_customer.model.GetConnectionGroupListResponse
import com.inensus.android.add_customer.model.GetConnectionTypeListResponse
import com.inensus.android.add_customer.model.GetManufacturerListResponse
import com.inensus.android.add_customer.model.GetMeterTypeListResponse
import com.inensus.android.add_customer.model.GetSubConnectionTypeListResponse
import com.inensus.android.add_customer.model.GetTariffListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface AddCustomerService {
    @GET
    fun getManufacturers(
        @Url url: String,
    ): Single<GetManufacturerListResponse>

    @GET
    fun getMeterTypes(
        @Url url: String,
    ): Single<GetMeterTypeListResponse>

    @GET
    fun getTariffs(
        @Url url: String,
    ): Single<GetTariffListResponse>

    @GET
    fun getCities(
        @Url url: String,
    ): Single<GetCityListResponse>

    @GET
    fun getConnectionGroups(
        @Url url: String,
    ): Single<GetConnectionGroupListResponse>

    @GET
    fun getConnectionTypes(
        @Url url: String,
    ): Single<GetConnectionTypeListResponse>

    @GET
    fun getSubConnectionTypes(
        @Url url: String,
    ): Single<GetSubConnectionTypeListResponse>
}
