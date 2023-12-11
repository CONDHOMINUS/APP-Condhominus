package com.example.condhominus.services

import com.example.condhominus.model.AddressResponse
import com.example.condhominus.model.CondominiumRegister
import com.example.condhominus.model.CondominiumResponse
import com.example.condhominus.model.AvailableSchedulesResponse
import com.example.condhominus.model.LoginResponse
import com.example.condhominus.model.TenantResponse
import com.example.condhominus.model.address.AddressResponse
import com.example.condhominus.model.condominium.CondominiumResponse
import com.example.condhominus.model.condominium.CondominiumsListResponse
import com.example.condhominus.model.login.LoginResponse
import com.example.condhominus.model.tenant.TenantResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CondhominusService {

    @POST("inquilino/cadastrar")
    suspend fun setTenant(@Body tenant: RequestBody): Response<TenantResponse>

    @GET("condominio/obterparalookup")
    suspend fun getCondominiums(): Response<CondominiumsListResponse>

    @POST("/autenticar/logar")
    suspend fun userLogin(@Body body: RequestBody): Response<LoginResponse>

    @POST("/condominio/cadastrar")
    suspend fun registerCondominium(@Body body: RequestBody): Response<CondominiumResponse>

    @GET("{zipCode}/json/")
    suspend fun getAddress(@Path("zipCode") zipCode: String): Response<AddressResponse>

    @GET("/AgendamentoMudanca/ObterAgendasDisponiveis")
    suspend fun getAvailableSchedules(): Response<AvailableSchedulesResponse>
}