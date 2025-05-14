package com.inensus.android.network.model

import okhttp3.Interceptor

class InterceptorsModel(
    val interceptors: List<Interceptor>,
    val networkInterceptors: List<Interceptor>,
)
