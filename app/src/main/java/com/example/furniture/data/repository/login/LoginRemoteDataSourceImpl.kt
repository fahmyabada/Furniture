package com.example.furniture.data.repository.login

import com.example.furniture.data.api.Api
import com.example.furniture.data.model.login.Login
import retrofit2.Response

class LoginRemoteDataSourceImpl(private val api: Api) : LoginRemoteDataSource {
    override suspend fun makeLogin(paramsMap: Map<String, String>): Response<Login> {
        return api.postLogin(paramsMap)
    }

}