package com.inensus.android.customer_list.model

import com.google.gson.annotations.SerializedName

data class AddCustomerResponse(
    @SerializedName("data") val data: Customer,
)
