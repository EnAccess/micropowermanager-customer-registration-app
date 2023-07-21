package com.inensus.android.add_customer.model

import com.google.gson.annotations.SerializedName

data class GetTariffListResponse(@SerializedName("data") val data: List<Tariff>)

data class Tariff(@SerializedName("id") var id: Int? = null,
                  @SerializedName("name") var name: String? = null,
                  @SerializedName("price") var price: Int? = null,
                  @SerializedName("currency") var currency: String? = null) {
    override fun toString(): String {
        return if ("Tariff" == name) "Tariff" else name + " (" + price?.div(100) + " " + currency + ")"
    }
}