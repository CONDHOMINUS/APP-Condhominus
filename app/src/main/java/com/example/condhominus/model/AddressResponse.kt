package com.example.condhominus.model

data class AddressResponse(
    val cep: String?,
    val logradouro: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?
)
