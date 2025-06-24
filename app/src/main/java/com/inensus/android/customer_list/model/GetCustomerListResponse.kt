package com.inensus.android.customer_list.model

import com.google.gson.annotations.SerializedName

// Add a model for pagination links if needed
data class PaginationLink(
    @SerializedName("url") val url: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("active") val active: Boolean?,
)

data class GetCustomerListResponse(
    @SerializedName("current_page") val currentPage: Int?,
    @SerializedName("data") val data: List<Customer>?,
    @SerializedName("first_page_url") val firstPageUrl: String?,
    @SerializedName("from") val from: Int?,
    @SerializedName("last_page") val lastPage: Int?,
    @SerializedName("last_page_url") val lastPageUrl: String?,
    @SerializedName("next_page_url") val nextPageUrl: String?,
    @SerializedName("path") val path: String?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("prev_page_url") val prevPageUrl: String?,
    @SerializedName("to") val to: Int?,
    @SerializedName("total") val total: Int?,
    @SerializedName("links") val links: List<PaginationLink>? = null,
)
