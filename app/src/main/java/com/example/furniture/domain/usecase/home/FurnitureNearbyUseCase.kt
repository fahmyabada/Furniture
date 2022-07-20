package com.example.furniture.domain.usecase.home

import com.example.furniture.data.model.furniture_nearby.FurnitureNearby
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.repository.HomeRepository

class FurnitureNearbyUseCase (private val homeRepository: HomeRepository) {

    suspend fun execute(paramsMap: Map<String, String>): Resource<FurnitureNearby> {
        return homeRepository.furnitureNearby(paramsMap)
    }
}