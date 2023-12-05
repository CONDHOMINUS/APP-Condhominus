package com.example.condhominus.services

import com.example.condhominus.model.LoginResponse
import com.example.condhominus.model.TenantResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface CondhominusService {

    @POST("Inquilino/Cadastrar")
    suspend fun setTenant(@Body tenant: RequestBody): Response<TenantResponse>

    @POST("/autenticar/logar")
    suspend fun userLogin(@Body body: RequestBody): Response<LoginResponse>

}