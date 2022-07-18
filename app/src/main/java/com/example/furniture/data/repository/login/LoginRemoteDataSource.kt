package com.example.furniture.data.repository.login

import com.example.furniture.data.model.login.Login
import retrofit2.Response

interface LoginRemoteDataSource {
    suspend fun makeLogin(paramsMap: Map<String, String>): Response<Login>
}