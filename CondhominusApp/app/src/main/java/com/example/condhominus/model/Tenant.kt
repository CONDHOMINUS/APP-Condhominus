package com.example.condhominus.model

data class Tenant(
    val condominio: Condominium,
    val numeroApartamento: String,
    val pessoa: Person
)

data class Condominium(
    val nome: String,
    val observacao: String,
    val endereco: Address
)

data class Address(
    val cep: String,
    val longradouro: String,
    val municipio: String,
    val uf: String,
    val pais: String,
    val pessoa: Person
)

data class Person(
    val nome: String,
    val dataNascimento: String
)

