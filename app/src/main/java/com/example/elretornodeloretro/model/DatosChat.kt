package com.example.elretornodeloretro.model

data class DatosChat(
    val ID_CHAT: Int,
    val ID_TIPO_CHAT: Int,
    val ID_USUARIO: Int,
    val NOMBRE_TIPO_CHAT: String,
    val TITULO: String,
    val NOMBRE: String,
    val APELLIDO: String,
    val ULTIMO_MENSAJE : Int
)