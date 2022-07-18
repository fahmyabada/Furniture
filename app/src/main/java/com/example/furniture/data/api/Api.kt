package com.example.furniture.data.api


import com.example.furniture.data.model.login.Login
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("login")
    suspend fun postLogin(
        @QueryMap paramsMap: Map<String, String>
    ): Response<Login>

}