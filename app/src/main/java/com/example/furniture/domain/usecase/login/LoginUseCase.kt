package com.example.furniture.domain.usecase.login

import com.example.furniture.data.model.login.Login
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.repository.LoginRepository


class LoginUseCase(private val loginRepository: LoginRepository) {

    suspend fun makeLogin(paramsMap: Map<String, String>): Resource<Login>{
        return loginRepository.makeLogin(paramsMap)
    }
}