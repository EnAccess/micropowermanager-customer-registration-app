package com.inensus.android.login.service

import com.inensus.android.login.model.LoginRequest
import com.inensus.android.login.model.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface LoginService {
    @POST
    fun login(@Url url: String, @Body request: LoginRequest): Single<LoginResponse>
}