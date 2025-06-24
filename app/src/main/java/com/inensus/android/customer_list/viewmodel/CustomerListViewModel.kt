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
    private val connectionChecker: ConnectionChecker,
) : BaseViewModel() {
    private var _uiState: MutableLiveData<CustomerListUiState> = MutableLiveData()
    val uiState: LiveData<CustomerListUiState> = _uiState

    private var currentPage = 1
    private var lastPage = 1
    private var isLoadingMore = false
    private val customers = mutableListOf<Customer>()

    init {
        getCustomers()
    }

    fun getCustomers() =
        repository
            .getCustomers()
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
                },
            ).addTo(compositeDisposable)

    fun deleteCustomerFromLocalStorage(
        customer: Customer,
        callback: (() -> Unit)? = null,
    ) {
        repository.deleteCustomerFromLocalStorage(customer, callback)
    }

    fun syncLocalStorage() {
        if (connectionChecker.isOnline && repository.localStorageCustomers.isNotEmpty()) {
            repository.localStorageCustomers.forEach {
                repository
                    .addCustomer(it)
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
                        },
                    ).addTo(compositeDisposable)
            }
        }
    }

    fun loadInitialCustomers() {
        currentPage = 1
        customers.clear()
        repository
            .getCustomersByPage(currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .subscribe(
                { response ->
                    hideLoading()
                    currentPage = response.currentPage ?: 1
                    lastPage = response.lastPage ?: 1
                    customers.clear()
                    response.data?.let { customers.addAll(it) }
                    if (customers.isNotEmpty()) {
                        _uiState.value = CustomerListUiState.Success(customers.toList())
                    } else {
                        _uiState.value = CustomerListUiState.Empty
                    }
                },
                {
                    _uiState.value = CustomerListUiState.Empty
                    handleError(it.toServiceError())
                },
            ).addTo(compositeDisposable)
    }

    fun loadNextPage() {
        if (isLoadingMore || currentPage >= lastPage) return
        isLoadingMore = true
        _uiState.value = CustomerListUiState.LoadingMore
        val nextPage = currentPage + 1
        repository
            .getCustomersByPage(nextPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    currentPage = response.currentPage ?: nextPage
                    lastPage = response.lastPage ?: lastPage
                    val newCustomers = response.data ?: emptyList()
                    customers.addAll(newCustomers)
                    _uiState.value = CustomerListUiState.Success(customers.toList())
                    isLoadingMore = false
                },
                { error ->
                    _uiState.value = CustomerListUiState.ErrorLoadingMore(error)
                    isLoadingMore = false
                },
            ).addTo(compositeDisposable)
    }
}
