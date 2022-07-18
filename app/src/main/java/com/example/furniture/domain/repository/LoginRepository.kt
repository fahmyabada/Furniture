package com.example.furniture.domain.repository

import com.example.furniture.data.model.login.Login
import com.example.furniture.data.util.Resource


interface LoginRepository {

    suspend fun makeLogin(paramsMap: Map<String, String>): Resource<Login>
}