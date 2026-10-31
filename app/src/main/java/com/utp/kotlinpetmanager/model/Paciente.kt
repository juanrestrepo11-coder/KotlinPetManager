package com.utp.kotlinpetmanager.model

data class Paciente(
    val id: Int,
    val nombre: String,
    val especie: String,
    val edad: Int,
    val contactoEmergencia: String,
    val esUrgencia: Boolean
)