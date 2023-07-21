package com.inensus.android.add_customer.model

import com.google.gson.annotations.SerializedName

data class GetCityListResponse(@SerializedName("data") val data: List<City>)

data class City(@SerializedName("id") var id: Int? = null,
                @SerializedName("name") var name: String? = null,
                @SerializedName("country_id") var countryId: Int? = null) {
    override fun toString(): String {
        return name!!
    }
}