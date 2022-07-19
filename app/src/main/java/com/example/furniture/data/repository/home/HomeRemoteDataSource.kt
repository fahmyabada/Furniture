package com.example.furniture.data.repository.home

import com.example.furniture.data.model.home.Home
import retrofit2.Response

interface HomeRemoteDataSource {
    suspend fun getHome(): Response<Home>
}