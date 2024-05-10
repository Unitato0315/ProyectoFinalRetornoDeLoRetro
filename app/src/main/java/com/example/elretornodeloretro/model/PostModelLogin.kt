package com.example.elretornodeloretro.model

import com.google.gson.annotations.SerializedName

data class PostModelLogin(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
