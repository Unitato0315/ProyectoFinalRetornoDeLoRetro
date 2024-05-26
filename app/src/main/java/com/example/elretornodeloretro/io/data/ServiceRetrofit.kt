package com.example.elretornodeloretro.io.data

import android.content.Context
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.Message
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.User
import com.example.elretornodeloretro.model.UserLogin
import com.example.elretornodeloretro.model.InformationOrder
import com.example.elretornodeloretro.model.ResponseOrder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path



interface ServiceRetrofit {
    @GET("api/game")
    suspend fun  listGames(): Array<Game>
    @POST("auth/login")
    suspend fun signIn(
        @Body p:PostModelLogin
    ): UserLogin

    @DELETE("api/game/{id}")
    suspend fun deleteGame(
        @Path("id")id:Int
    ): Response<Void>

    @POST("usuario")
    suspend fun signUp(
        @Body u:User
    ): Response<Message>

    @POST()
    suspend fun createOrder(
        @Body o:InformationOrder
    ):Response<ResponseOrder>

    @GET()
    suspend fun getOrder(
        @Path("id")id:Int
    ): Response<Void>

    @GET()
    suspend fun getProductOrder(

    )
}

object RetrofitServiceFactory{
    fun makeRetrofitService(context: Context): ServiceRetrofit{

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://base-datos-mysql-f626301842d3.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(ServiceRetrofit::class.java)
    }
}