package com.example.furniture.data.repository.home

import com.example.furniture.data.api.Api
import com.example.furniture.data.model.home.Home
import retrofit2.Response

class HomeRemoteDataSourceImpl(private val api: Api) :HomeRemoteDataSource {

    override suspend fun getHome(): Response<Home> {
        return api.getHome()
    }

}