package com.inensus.android.add_customer.model

import com.google.gson.annotations.SerializedName

data class GetManufacturerListResponse(
    @SerializedName("data") val data: List<Manufacturer>,
)

data class Manufacturer(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
) {
    override fun toString(): String = name!!
}
