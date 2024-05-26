package com.example.elretornodeloretro.model

data class ResponseOrder(
    val message: String,
    val productos: List<Producto>,
    val success: Boolean
)