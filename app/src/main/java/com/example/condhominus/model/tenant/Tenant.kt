package com.example.condhominus.model.tenant

data class Tenant(
    val numeroApartamento: String,
    val condominioId: Int,
    val pessoa: Person
)

data class Person(
    val nomeCompleto: String,
    val cpfCnpj: String,
    val dataNascimento: String,
    val tipoPessoa: Int? = 0
)
