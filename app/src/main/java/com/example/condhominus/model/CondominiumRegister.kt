package com.example.condhominus.model

data class CondominiumRegister(
    val nome: String,
    val observacao: String,
    val endereco: Endereco
)

data class Endereco(
    val cep: String,
    val logradouro: String,
    val municipio: String,
    val uf: String,
    val pais: String,
    val numero: String,
    val principal: Boolean? = true
)