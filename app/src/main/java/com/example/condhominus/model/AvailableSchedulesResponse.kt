package com.example.condhominus.model

data class AvailableSchedulesResponse (
    val sucesso: Boolean,
    val mensagemErro: String,
    val agendas: List<Schedule>
)

data class Schedule(
    val data: String,
    val periodos: Periodos
)

data class Periodos(
    val periodoManha: Int,
    val periodoTarde: Int
)