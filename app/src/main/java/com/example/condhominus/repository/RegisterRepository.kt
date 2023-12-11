package com.example.condhominus.repository

import com.example.condhominus.model.condominium.CondominiumRegister
import com.example.condhominus.model.condominium.CondominiumResponse
import com.example.condhominus.model.condominium.CondominiumsListResponse
import com.example.condhominus.model.tenant.Tenant
import com.example.condhominus.model.tenant.TenantResponse
import com.example.condhominus.services.CondhominusService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://condhominus.somee.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(CondhominusService::class.java)

    suspend fun registerTenant(tenant: Tenant): TenantResponse? {
        return withContext(Dispatchers.Default) {
            processData(service.setTenant(RequestBody.create(MediaType.parse("application/json"), Gson().toJson(tenant))))
        }
    }

    suspend fun getCondominiums(): CondominiumsListResponse? {
        return withContext(Dispatchers.Default) {
            processData(service.getCondominiums())
        }
    }

    suspend fun registerCondominium(condominiumRegister: CondominiumRegister): CondominiumResponse? {
        return withContext(Dispatchers.Default) {
            println("Body: ${RequestBody.create(MediaType.parse("application/json"), Gson().toJson(condominiumRegister))}")
            processData(service.registerCondominium(RequestBody.create(MediaType.parse("application/json"), Gson().toJson(condominiumRegister))))
        }
    }

    private fun <T> processData(response: Response<T>): T? {
        return if (response.isSuccessful) response.body()
        else null
    }
}