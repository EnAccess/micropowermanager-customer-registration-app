package com.inensus.android.add_customer.model

import com.google.gson.annotations.SerializedName

data class GetMeterTypeListResponse(
    @SerializedName("data") val data: List<MeterType>,
)

data class MeterType(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("online") var online: Int? = null,
    @SerializedName("phase") var phase: Int? = null,
    @SerializedName("max_current") var maxCurrent: Int? = null,
) {
    override fun toString(): String =
        if (id!! < 0) "Meter Type" else maxCurrent.toString() + "A " + phase + "P" + " " + if (online == 1) "Online" else "Offline"
}
