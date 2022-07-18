package com.example.furniture.data.api

import okhttp3.OkHttpClient

class HeaderInterceptor {

    fun intercept(sessionManager: SessionManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader(
                        "Authorization", "Bearer ${sessionManager.getString("token")}"
                    )
                    .method(chain.request().method, chain.request().body)
                    .build()
                chain.proceed(newRequest)
            }.build()
    }
}