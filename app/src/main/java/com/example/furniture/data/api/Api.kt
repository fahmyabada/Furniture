package com.example.furniture.data.api


import com.example.furniture.data.model.furniture_nearby.FurnitureNearby
import com.example.furniture.data.model.home.Home
import com.example.furniture.data.model.login.Login
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @POST("login")
    suspend fun postLogin(
        @QueryMap paramsMap: Map<String, String>
    ): Response<Login>

    @GET("home")
    suspend fun getHome(): Response<Home>

    @POST("home/furniture-nearby")
    suspend fun furnitureNearby(
        @QueryMap paramsMap: Map<String, String>
    ): Response<FurnitureNearby>
}