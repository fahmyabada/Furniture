package com.example.furniture.data.repository.home

import com.example.furniture.data.model.furniture_nearby.FurnitureNearby
import com.example.furniture.data.model.home.Home
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.repository.HomeRepository
import retrofit2.Response

class HomeRepositoryImpl(private val homeRemoteDataSource: HomeRemoteDataSource) :
    HomeRepository {

    override suspend fun getHome(): Resource<Home> {
        return responseHomeToResource(homeRemoteDataSource.getHome())
    }

    override suspend fun furnitureNearby(paramsMap: Map<String, String>): Resource<FurnitureNearby> {
        return responseFurnitureNearbyToResource(homeRemoteDataSource.furnitureNearby(paramsMap))
    }

    private fun responseHomeToResource(response: Response<Home>): Resource<Home> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun responseFurnitureNearbyToResource(response: Response<FurnitureNearby>): Resource<FurnitureNearby> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}