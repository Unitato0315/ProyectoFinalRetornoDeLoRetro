package com.example.elretornodeloretro.model

data class Order(
    val APELLIDO: String,
    val CP: Int,
    val DIRECCION: String,
    val EMAIL: String,
    val FECHA: String,
    val FORMA_DE_PAGO: String,
    val ID_PEDIDO: Int,
    val ID_USUARIO: Int,
    val LOCALIDAD: String,
    val NOMBRE: String,
    val PRECIO: Double,
    val PROVINCIA: String,
    val TELEFONO: Int,
    val TIPO_ENVIO: String
)