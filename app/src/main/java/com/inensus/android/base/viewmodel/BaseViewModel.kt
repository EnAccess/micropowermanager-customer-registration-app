package com.inensus.android.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inensus.android.base.model.ServiceError
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<ServiceError>()
    val error: LiveData<ServiceError> = _error

    val compositeDisposable = CompositeDisposable()

    fun handleError(error: ServiceError) {
        _error.value = error
        hideLoading()
    }

    fun showLoading() {
        _loading.value = true
    }

    fun hideLoading() {
        _loading.value = false
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
