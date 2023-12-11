package com.example.condhominus.repository

import com.example.condhominus.model.AvailableSchedulesResponse
import com.example.condhominus.services.CondhominusService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AvailableSchedulesRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://condhominus.somee.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(CondhominusService::class.java)

    suspend fun getAvailableSchedules(): AvailableSchedulesResponse? {
        return withContext(Dispatchers.Default) {
            processData(service.getAvailableSchedules())
        }
    }

    private fun <T> processData(response: Response<T>): T? {
        return if (response.isSuccessful) response.body()
        else null
    }
}