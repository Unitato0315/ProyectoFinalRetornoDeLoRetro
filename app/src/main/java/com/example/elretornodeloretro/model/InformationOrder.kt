package com.example.elretornodeloretro.model

data class InformationOrder(
    val cardNumber: Long,
    val cp: Int,
    val cvv: Int,
    val direccion: String,
    val expiryDate: Int,
    val idUsuario: Int,
    val localidad: String,
    val productId: MutableList<Int>,
    val provincia: String,
    val tipoEnvio: Int,
    val tipoPago: Int,
    val total: Float
)