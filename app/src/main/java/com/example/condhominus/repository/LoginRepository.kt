package com.example.condhominus.repository

import com.example.condhominus.model.login.LoginBody
import com.example.condhominus.model.login.LoginResponse
import com.example.condhominus.services.CondhominusService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://condhominus.somee.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(CondhominusService::class.java)

    suspend fun userLogin(login: LoginBody): LoginResponse? {
        return withContext(Dispatchers.Default) {
            processData(service.userLogin(RequestBody.create(MediaType.parse("application/json"), Gson().toJson(login))))
        }
    }

    private fun <T> processData(response: Response<T>): T? {
        return if (response.isSuccessful) response.body()
        else null
    }
}