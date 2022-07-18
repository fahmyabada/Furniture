package com.example.furniture.domain.repository

import com.example.furniture.data.model.login.Login
import com.example.furniture.data.util.Resource

interface RegisterRepository {
    suspend fun makeRegister(paramsMap: Map<String, String>): Resource<Login>
}