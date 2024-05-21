package com.example.elretornodeloretro.interceptor

import android.content.Context
import android.util.Log
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.model.Almacen
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    val context = context

    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenManage = TokenManage(context)

        val token = tokenManage.getToken().toString()
        Log.e("JVVM",token)
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