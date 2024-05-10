package com.example.elretornodeloretro.io.data

import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.ModelDeleteGame
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.UserLogin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceRetrofit {
    @GET("api/game")
    suspend fun  listGames(): Array<Game>
    @POST("auth/login")
    suspend fun signIn(
        @Body p:PostModelLogin
    ): UserLogin
    @Headers("auth : 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVU0VSTkFNRSI6IlBBUVVJVE8iLCJJRF9ST0wiOjk5LCJpYXQiOjE3MTUwNjg2MjgsImV4cCI6MTcxNTA5MDIyOH0.teEVuWWjhoE_3qU49OyfOluxR9YVJoxp3YhOynwLqpU'")
    @DELETE("api/game/{id}")
    suspend fun deleteGame(
        @Path("id")id:Int,
        @Path("token")token:String
    ): ModelDeleteGame
}

object RetrofitServiceFactory{
    fun makeRetrofitService(): ServiceRetrofit{
        return Retrofit.Builder()
            .baseUrl("https://base-datos-mysql-f626301842d3.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ServiceRetrofit::class.java)
    }
}