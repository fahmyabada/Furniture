package com.example.furniture.data.repository.login

import com.example.furniture.data.model.login.Login
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.repository.LoginRepository
import retrofit2.Response

class LoginRepositoryImpl(private val LoginRemoteDataSource: LoginRemoteDataSource) :
    LoginRepository {
    override suspend fun makeLogin(paramsMap: Map<String, String>): Resource<Login> {
        return responseLoginToResource(LoginRemoteDataSource.makeLogin(paramsMap))
    }



    private fun responseLoginToResource(response: Response<Login>): Resource<Login> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}