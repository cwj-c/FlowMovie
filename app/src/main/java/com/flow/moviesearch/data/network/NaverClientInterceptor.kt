package com.flow.moviesearch.data.network

import com.flow.moviesearch.BuildConfig
import com.flow.moviesearch.data.constant.NetworkConstant
import okhttp3.Interceptor
import okhttp3.Response

class NaverClientInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request().newBuilder()
            .addHeader(NetworkConstant.CLIENT_ID_HEADER, BuildConfig.API_CLIENT_ID)
            .addHeader(NetworkConstant.CLIENT_SECRET_HEADER, BuildConfig.API_CLIENT_SECRET)
            .build().let {
                chain.proceed(it)
            }
    }
}