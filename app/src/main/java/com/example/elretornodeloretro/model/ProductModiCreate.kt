package com.example.elretornodeloretro.model

data class ProductModiCreate(
    val DESCRIPCION: String,
    val ID_PLATAFORMA: Int,
    val ID_TIPO: Int,
    val IMAGEN: String,
    val PRECIO_FINAL: Float,
    val TITULO: String
)