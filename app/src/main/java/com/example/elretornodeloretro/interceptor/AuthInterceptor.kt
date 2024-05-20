package com.example.elretornodeloretro.Interceptor

import android.content.Context
import com.example.elretornodeloretro.model.Almacen
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val token = Almacen.token
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