package com.inensus.android.customer_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.android.base.viewmodel.BaseViewModel
import com.inensus.android.customer_list.model.Customer
import com.inensus.android.customer_list.service.CustomerListRepository
import com.inensus.android.customer_list.view.CustomerListUiState
import com.inensus.android.extensions.toServiceError
import com.inensus.android.util.ConnectionChecker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CustomerListViewModel(
    private val repository: CustomerListRepository,
    private val connectionChecker: ConnectionChecker
) : BaseViewModel() {

    private var _uiState: MutableLiveData<CustomerListUiState> = MutableLiveData()
    val uiState: LiveData<CustomerListUiState> = _uiState

    init {
        getCustomers()
    }

    fun getCustomers() = repository.getCustomers()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { showLoading() }
        .subscribe(
            {
                hideLoading()
                if (it.isNotEmpty()) {
                    _uiState.value = CustomerListUiState.Success(it)
                } else {
                    _uiState.value = CustomerListUiState.Empty
                }
            },
            {
                _uiState.value = CustomerListUiState.Empty
                handleError(it.toServiceError())
            }
        ).addTo(compositeDisposable)

    fun deleteCustomerFromLocalStorage(customer: Customer, callback: (() -> Unit)? = null) {
        repository.deleteCustomerFromLocalStorage(customer, callback)
    }

    fun syncLocalStorage() {
        if (connectionChecker.isOnline && repository.localStorageCustomers.isNotEmpty()) {

            repository.localStorageCustomers.forEach {
                repository.addCustomer(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { showLoading() }
                    .subscribe(
                        { customer ->
                            hideLoading()
                            repository.deleteCustomerFromLocalStorage(customer)
                            _uiState.value = CustomerListUiState.AddCustomerSuccess
                        },
                        { throwable ->
                            hideLoading()
                            _uiState.value = CustomerListUiState.AddCustomerError(throwable, it)
                        }
                    ).addTo(compositeDisposable)
            }
        }
    }
}
