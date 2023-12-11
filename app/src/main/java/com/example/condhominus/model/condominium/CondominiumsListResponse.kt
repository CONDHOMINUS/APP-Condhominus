package com.example.condhominus.model.condominium

data class CondominiumsListResponse(val condominios: List<CondominiumItem>?)

data class CondominiumItem(
    val idCondominio: Int,
    val descricaoCondominio: String
)