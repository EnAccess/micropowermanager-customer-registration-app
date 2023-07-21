package com.inensus.android.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.android.base.lifecycle.LiveEvent
import com.inensus.android.base.viewmodel.BaseViewModel
import com.inensus.android.extensions.toServiceError
import com.inensus.android.login.repository.LoginRepository
import com.inensus.android.login.view.LoginFormValidator
import com.inensus.android.login.view.LoginUiState
import com.inensus.android.util.SharedPreferenceWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo

class LoginViewModel(private val repository: LoginRepository, private val validator: LoginFormValidator, private val preferences: SharedPreferenceWrapper) : BaseViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _uiState = LiveEvent<LoginUiState>()
    val uiState: LiveData<LoginUiState> = _uiState

    init {
        _uiState.postValue(LoginUiState.ServerUrl(preferences.baseUrl.isNullOrEmpty()))
    }

    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onLoginButtonTapped() {
        if (preferences.baseUrl.isNullOrEmpty()) {
            _uiState.value = LoginUiState.ServerUrl(preferences.baseUrl.isNullOrEmpty())
        } else {
            validator.validateForm(_email.value, _password.value).let {
                if (it.isEmpty()) {
                    login()
                } else {
                    _uiState.value = LoginUiState.ValidationError(it)
                }
            }
        }
    }

    private fun login() {
        showLoading()

        repository.login(_email.value, _password.value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    hideLoading()
                    _uiState.value = LoginUiState.Success
                }, {
                    handleError(it.toServiceError())
                })
                .addTo(compositeDisposable)
    }
}