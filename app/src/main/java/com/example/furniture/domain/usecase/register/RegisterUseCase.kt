package com.example.furniture.domain.usecase.register

import com.example.furniture.data.model.login.Login
import com.example.furniture.data.util.Resource
import com.example.furniture.domain.repository.RegisterRepository

class RegisterUseCase(private val registerRepository: RegisterRepository) {
    suspend fun makeRegister(paramsMap: Map<String, String>): Resource<Login>{
        return registerRepository.makeRegister(paramsMap)
    }
}