package com.example.elretornodeloretro

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.res.Resources
import androidx.lifecycle.lifecycleScope
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.elretornodeloretro.adapter.AdapterViewPage
import com.example.elretornodeloretro.databinding.ActivityMainBinding
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.UserLogin
import kotlinx.coroutines.launch
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.model.Almacen
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.HttpException

//import com.google.common.io.Resources


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var TAG ="JVVM"
    lateinit var tokenManage: TokenManage

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = RetrofitServiceFactory.makeRetrofitService(this)

        tokenManage = TokenManage(this)

        tokenManage.deleteToken()
        //val textToken = tokenManage.getToken()

        //if(textToken.isNullOrBlank()){
        //    Toast.makeText(this,"No guardo el token",Toast.LENGTH_SHORT).show()
        //}else{
        //   Toast.makeText(this,"Guardo el token",Toast.LENGTH_SHORT).show()
        //}
        val context = this
        //Con esto me permite realizar el login de la pagina (metodo post necesitan crear un modelo para poder enviarlo como un json, usar el serializableName para ello)
        //lifecycleScope.launch {
         //   val objRequest = PostModelLogin(
         //       username = "PAQUITO",
         //       password = "PRUEBA")

         //   try {
         //       val response = service.signIn(objRequest)
         //       handleLoginSuccess(response)
         //   }catch (e:HttpException){
         //       if(e.code()==400){
         //           handleLoginError()
         //       }else{
         //           Toast.makeText(context,"Se ha producido un error",Toast.LENGTH_SHORT).show()
         //       }
         //   }catch (e: Exception){
         //       Toast.makeText(context,"Fallo en la solicitud",Toast.LENGTH_SHORT).show()
         //   }

        //}

        lifecycleScope.launch {
            val listGames = service.listGames()
            runOnUiThread {
                //showGamesPruebas(listGames)
                Almacen.games = listGames
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

                binding.vpPrincipal.adapter = AdapterViewPage(context)
                TabLayoutMediator(binding.tabLayout,binding.vpPrincipal){tab,index->
                    tab.text = when(index){
                        0-> ""
                        1-> ""
                        else -> ""
                    }
                    tab.icon = when(index){
                        0-> getDrawable(R.drawable.gamepad_solid)
                        1-> getDrawable(R.drawable.user_solid)
                        else -> {throw Resources.NotFoundException("Posicion no encontrada")}
                    }
                }.attach()
            }
        }

    }
    private fun deleteGame(id: Int) {
        val context = this
        lifecycleScope.launch {
            val request = RetrofitServiceFactory.makeRetrofitService(context).deleteGame(id)
            try {
                val response = RetrofitServiceFactory.makeRetrofitService(context).deleteGame(id)
                if (response.isSuccessful)  {
                    Toast.makeText(context, "No tienes acceso a esta seccion", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "No tienes acceso a esta seccion", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Toast.makeText(context, "Se ha producido un error: ${e.message}", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun handleLoginSuccess(successResponse: UserLogin?) {
        if (successResponse != null) {
            Toast.makeText(this@MainActivity,"Se ha iniciado sesion correctamente",Toast.LENGTH_SHORT).show()
            tokenManage.saveToken(successResponse.token)
        }
    }

    private fun handleLoginError() {

        Toast.makeText(this@MainActivity,"Se han introducido mal los datos",Toast.LENGTH_SHORT).show()

    }

    fun showGamesPruebas(games:Array<Game>){
        for (game in games){
            Toast.makeText(this,game.TITULO,Toast.LENGTH_SHORT).show()
        }
    }

    fun showToken(result: UserLogin){
        Toast.makeText(this,"'"+result.message+"'",Toast.LENGTH_LONG).show()
    }

    fun obtenerAlgoritmo(token: String): String? {
        try {
            val jwt: DecodedJWT = JWT.decode(token)
            return jwt.algorithm
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun decodeToken(context: Context, token: String){
        val claims = GeneralFuntion.decodeJWT(token)
        if (claims != null) {
            val username = claims["USERNAME"]?.toString()
            val id_user = claims["ID_USUARIO"]
            val id_rol = claims["ID_ROL"]?.toString()
            // Obtener otros datos según los campos del token
            Toast.makeText(context,username,Toast.LENGTH_SHORT).show()
            Toast.makeText(context,id_user.toString(),Toast.LENGTH_SHORT).show()
            Toast.makeText(context,id_rol,Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,"NO SE A PODIDO DECODIFICAR",Toast.LENGTH_SHORT).show()
        }
    }
}