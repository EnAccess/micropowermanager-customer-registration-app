package com.inensus.android.add_customer.service

import com.inensus.android.add_customer.model.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface AddCustomerService {

    @GET
    fun getManufacturers(@Url url: String): Single<GetManufacturerListResponse>

    @GET
    fun getMeterTypes(@Url url: String): Single<GetMeterTypeListResponse>

    @GET
    fun getTariffs(@Url url: String): Single<GetTariffListResponse>

    @GET
    fun getCities(@Url url: String): Single<GetCityListResponse>

    @GET
    fun getConnectionGroups(@Url url: String): Single<GetConnectionGroupListResponse>

    @GET
    fun getConnectionTypes(@Url url: String): Single<GetConnectionTypeListResponse>

    @GET
    fun getSubConnectionTypes(@Url url: String): Single<GetSubConnectionTypeListResponse>
}