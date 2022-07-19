package com.example.furniture.domain.repository

import com.example.furniture.data.model.home.Home
import com.example.furniture.data.util.Resource


interface HomeRepository {

    suspend fun getHome(): Resource<Home>
}