package com.example.elretornodeloretro.model

data class Tipo(
    val ID_TIPO: Int,
    val NOMBRE_TIPO: String
){
    override fun toString(): String {
        return NOMBRE_TIPO
    }
}