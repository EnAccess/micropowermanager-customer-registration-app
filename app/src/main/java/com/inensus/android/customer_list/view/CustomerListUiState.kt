package com.inensus.android.customer_list.view

import com.inensus.android.customer_list.model.Customer

sealed class CustomerListUiState {
    data class Success(
        val customers: List<Customer>,
    ) : CustomerListUiState()

    object Empty : CustomerListUiState()

    object AddCustomerSuccess : CustomerListUiState()

    data class AddCustomerError(
        val throwable: Throwable,
        val customer: Customer,
    ) : CustomerListUiState()
}
