package com.inensus.android.add_customer.model

import com.google.gson.annotations.SerializedName

data class GetConnectionTypeListResponse(
    @SerializedName("data") val data: List<ConnectionType>,
)

data class ConnectionType(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
)
