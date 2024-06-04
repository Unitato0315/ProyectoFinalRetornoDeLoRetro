package com.example.elretornodeloretro.model

object Almacen {
    var totalPrice: Float = 0f
    var token: String = ""
    var games: Array<Game> = arrayOf()
    var cart: MutableList<Game> = mutableListOf()
    lateinit var selectecGame : Game
    var tipesPays: Array<TipePay> = arrayOf()
    var tipesSend: Array<TipeSend> = arrayOf()
    var startSession = 0
    var pedidos: MutableList<Order> = mutableListOf()
    var gamesPedidos: MutableList<ProductOrder> = mutableListOf()
    lateinit var selectecPedido : Order
    var tipos: MutableList<Tipo> = mutableListOf()
    var plataformas : MutableList<Plataforma> = mutableListOf()
    var listaChat : MutableList<DatosChat> = mutableListOf()
}