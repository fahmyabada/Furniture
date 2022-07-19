package com.example.furniture.data.repository.home

import com.example.furniture.data.model.home.Home
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.repository.HomeRepository
import retrofit2.Response

class HomeRepositoryImpl(private val homeRemoteDataSource: HomeRemoteDataSource) :
    HomeRepository {

    override suspend fun getHome(): Resource<Home> {
        return responseHomeToResource(homeRemoteDataSource.getHome())
    }

    private fun responseHomeToResource(response: Response<Home>): Resource<Home> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

}