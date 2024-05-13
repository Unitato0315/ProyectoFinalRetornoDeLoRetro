package com.example.elretornodeloretro.io

import android.content.Context

class TokenManage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("mi_app_pref", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveToken(token: String) {
        editor.putString("jwt_token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun deleteToken() {
        editor.remove("jwt_token")
        editor.apply()
    }
}