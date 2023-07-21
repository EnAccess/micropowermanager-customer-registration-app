package com.inensus.android.base.model

import com.google.gson.annotations.SerializedName

data class ServiceError(@SerializedName("data") val error: Error?)

data class Error(val message: String)