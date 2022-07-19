package com.example.furniture.domain.usecase.home

import com.example.furniture.data.model.home.Home
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.repository.HomeRepository


class GetHomeUseCase(private val homeRepository: HomeRepository) {

    suspend fun getHome(): Resource<Home>{
        return homeRepository.getHome()
    }
}