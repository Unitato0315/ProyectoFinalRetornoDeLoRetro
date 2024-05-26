package com.example.elretornodeloretro.model

data class InformationOrder(
    val cardNumber: String,
    val cp: Int,
    val cvv: String,
    val direccion: String,
    val expiryDate: String,
    val idUsuario: Int,
    val localidad: String,
    val productId: List<Int>,
    val provincia: String,
    val tipoEnvio: Int,
    val tipoPago: Int
)