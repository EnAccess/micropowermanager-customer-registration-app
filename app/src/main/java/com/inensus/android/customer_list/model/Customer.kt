package com.inensus.android.customer_list.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Customers")
data class Customer(
        @SerializedName("id") @field:PrimaryKey(autoGenerate = true) @field:ColumnInfo(name = "id") var id: Int? = null,
        @SerializedName("name") @field:ColumnInfo(name = "name") var name: String? = null,
        @SerializedName("surname") @field:ColumnInfo(name = "surname") var surname: String? = null,
        @SerializedName("phone") @field:ColumnInfo(name = "phone") var phone: String? = null,
        @SerializedName("tariff_id") @field:ColumnInfo(name = "tariff") var tariff: Int? = null,
        @SerializedName("city_id") @field:ColumnInfo(name = "city") var city: Int? = null,
        @SerializedName("connection_group_id") @field:ColumnInfo(name = "connection_group") var connectionGroup: Int? = null,
        @SerializedName("connection_type_id") @field:ColumnInfo(name = "connection_type") var connectionType: Int? = null,
        @SerializedName("sub_connection_type_id") @field:ColumnInfo(name = "sub_connection_type") var subConnectionType: Int? = null,
        @SerializedName("geo_points") @field:ColumnInfo(name = "geo_points") var geoPoints: String? = null,
        @SerializedName("serial_number") @field:ColumnInfo(name = "serial_number") var serialNumber: String? = null,
        @SerializedName("manufacturer") @field:ColumnInfo(name = "manufacturer") var manufacturer: Int? = null,
        @SerializedName("meter_type") @field:ColumnInfo(name = "meter_type") var meterType: Int? = null,
        @SerializedName("local") @field:ColumnInfo(name = "isLocal") var isLocal: Boolean = false)