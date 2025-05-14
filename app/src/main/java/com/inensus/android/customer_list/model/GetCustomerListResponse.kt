package com.inensus.android.customer_list.model

import com.google.gson.annotations.SerializedName

data class GetCustomerListResponse(
    @SerializedName("data") val data: List<Customer>,
)
