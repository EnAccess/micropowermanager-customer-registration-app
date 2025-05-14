package com.inensus.android.login.repository

import com.inensus.android.login.model.LoginRequest
import com.inensus.android.login.service.LoginService
import com.inensus.android.util.SharedPreferenceWrapper

class LoginRepository(
    private val service: LoginService,
    private val preferences: SharedPreferenceWrapper,
) {
    fun login(
        email: String?,
        password: String?,
    ) = service
        .login(preferences.baseUrl + LOGIN_ENDPOINT, LoginRequest(email, password))
        .doOnSuccess { preferences.accessToken = it.accessToken }

    companion object {
        private const val LOGIN_ENDPOINT = "auth/login"
    }
}
