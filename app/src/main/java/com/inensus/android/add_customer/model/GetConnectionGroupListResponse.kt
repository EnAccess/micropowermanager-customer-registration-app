package com.inensus.android.add_customer.model

import com.google.gson.annotations.SerializedName

data class GetConnectionGroupListResponse(
    @SerializedName("data") val data: List<ConnectionGroup>,
)

data class ConnectionGroup(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
)
