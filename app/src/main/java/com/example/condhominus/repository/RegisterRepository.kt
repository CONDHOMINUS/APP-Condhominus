package com.example.condhominus.repository

import com.example.condhominus.model.Tenant
import com.example.condhominus.model.TenantResponse
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
        .baseUrl("https://condhominus.azurewebsites.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(CondhominusService::class.java)
    suspend fun registerTenant(tenant: Tenant): TenantResponse? {
        return withContext(Dispatchers.Default) {
            processData(service.setTenant(RequestBody.create(MediaType.parse("application/json"), Gson().toJson(tenant))))
        }
    }

    private fun <T> processData(response: Response<T>): T? {
        return if (response.isSuccessful) response.body()
        else null
    }
}