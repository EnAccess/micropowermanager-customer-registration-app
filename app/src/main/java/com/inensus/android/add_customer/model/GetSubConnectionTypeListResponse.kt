package com.inensus.android.add_customer.model

import com.google.gson.annotations.SerializedName

data class GetSubConnectionTypeListResponse(@SerializedName("data") val data: List<SubConnectionType>)

data class SubConnectionType(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("connection_type_id") var connectionTypeId: Int? = null
)
