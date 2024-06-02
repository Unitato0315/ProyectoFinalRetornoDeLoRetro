package com.example.elretornodeloretro.io.data

import android.content.Context
import com.example.elretornodeloretro.model.Estado
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.Message
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.User
import com.example.elretornodeloretro.model.UserLogin
import com.example.elretornodeloretro.model.InformationOrder
import com.example.elretornodeloretro.model.Order
import com.example.elretornodeloretro.model.Plataforma
import com.example.elretornodeloretro.model.ProductModiCreate
import com.example.elretornodeloretro.model.ProductOrder
import com.example.elretornodeloretro.model.ResponseOrder
import com.example.elretornodeloretro.model.TipePay
import com.example.elretornodeloretro.model.TipeSend
import com.example.elretornodeloretro.model.Tipo
import com.example.elretornodeloretro.model.UserData
import com.example.elretornodeloretro.model.UserDataModify
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("tiposEnvios")
    suspend fun getTipeSend(
    ): Array<TipeSend>

    @GET("tiposPagos")
    suspend fun getTipePay(
    ): Array<TipePay>

    @POST("pago")
    suspend fun createOrder(
        @Body o:InformationOrder
    ):ResponseOrder

    @GET("pago/{id}")
    suspend fun getOrder(
        @Path("id")id:Int
    ): Array<Order>

    @GET("pagos")
    suspend fun getAllOrder(
    ): Array<Order>

    @GET("pago/productos/{id}")
    suspend fun getProductOrder(
        @Path("id")id:Int
    ):Array<ProductOrder>

    @PUT("cambiarEstado/{id}")
    suspend fun setEstado(
        @Path("id")id:Int,
        @Body o: Estado
    ):Message

    @GET("usuario/{id}")
    suspend fun getUser(
        @Path("id")id:Int
    ):Array<UserData>

    @PUT("datosFacturacion/{id}")
    suspend fun setDatosUser(
        @Path("id")id:Int,
        @Body o: UserDataModify
    ):Message

    @GET("api/tipos")
    suspend fun getTipos(
    ):Array<Tipo>

    @GET("api/plataforma")
    suspend fun getPlataforma(
    ):Array<Plataforma>

    @POST("api/game")
    suspend fun createGame(
        @Body o:ProductModiCreate
    ):Message

    @PUT("api/game/{id}")
    suspend fun modifyGame(
        @Path("id")id:Int,
        @Body o:ProductModiCreate
    ):Message

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