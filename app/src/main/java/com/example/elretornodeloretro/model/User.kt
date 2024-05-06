package com.example.elretornodeloretro.model

/*
*       "ID_USUARIO": 25,
*       "USERNAME": "unitato0315",
*       "PASSWORD": "$2a$10$SZa0g3VmdYSVuaXV81vRv.xfkP8fjC/kpN/l3RidD0ZuSHRPojzkS",
*       "NOMBRE": "Jose Vicente",
*       "APELLIDO": "Vargas Mestanza",
*       "EMAIL": "jvsonic9@gmail.com",
*       "TELEFONO": 999999999,
*       "ID_ROL": 1,
*       "DNI": "12345678A",
*       "PROVINCIA": "Almagro",
*       "LOCALIDAD": "Almagro",
*       "DIRECCION": "C/ De La Rosa",
*       "CODIGO_POSTAL": 13270
*
*
* */
data class User(
    val id: Int,
    val username: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: Int,
    val id_rol: Int,
    val dni : String,
    val provincia: String,
    val localidad: String,
    val direccion: String,
    val codigo_postal: Int
)
