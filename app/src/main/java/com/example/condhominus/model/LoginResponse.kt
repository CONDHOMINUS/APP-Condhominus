package com.example.condhominus.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("login")
    val login: Login?
)

data class Login(
    val idUsuario: Long,
    val nomeUsuario: String,
    val administrador: Boolean
)