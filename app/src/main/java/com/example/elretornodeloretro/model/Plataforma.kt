package com.example.elretornodeloretro.model

data class Plataforma(
    val ID_PLATAFORMA: Int,
    val NOMBRE_PLATAFORMA: String
){
    override fun toString(): String {
        return NOMBRE_PLATAFORMA
    }
}