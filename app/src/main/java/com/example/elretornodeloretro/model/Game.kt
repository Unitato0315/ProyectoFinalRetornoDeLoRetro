package com.example.elretornodeloretro.model

data class Game(
    val DESCRIPCION: String,
    val FECHA_PUBLICACION: String,
    val ID_ESTADO_PRODUCTO: Int,
    val ID_PLATAFORMA: Int,
    val ID_PRODUCTO: Int,
    val ID_TIPO: Int,
    val IMAGEN: String,
    val NOMBRE_ESTADO_PRODUCTO: String,
    val NOMBRE_PLATAFORMA: String,
    val NOMBRE_TIPO: String,
    val PRECIO_FINAL: Float,
    val PRECIO_VENDEDOR: Float,
    val TITULO: String
)