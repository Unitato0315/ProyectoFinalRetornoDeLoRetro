package com.example.elretornodeloretro.io.data

import android.content.Context
import android.util.Log
import com.example.elretornodeloretro.io.TokenManage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    val context = context
    // Se encarga de introducir dentro de la llamada a el servidor el token
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenManage = TokenManage(context)

        val token = tokenManage.getToken().toString()
        val request = if(token != ""){
            chain.request().newBuilder()
                .addHeader("auth", token)
                .build()
        }else{
            chain.request()
        }

        val response = chain.proceed(request)

        return response
    }
}