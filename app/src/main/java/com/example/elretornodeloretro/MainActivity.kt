package com.example.elretornodeloretro

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.res.Resources
import androidx.lifecycle.lifecycleScope
import com.example.elretornodeloretro.adapter.AdapterViewPage
import com.example.elretornodeloretro.databinding.ActivityMainBinding
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.UserLogin
import kotlinx.coroutines.launch
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.google.android.material.tabs.TabLayoutMediator
//import com.google.common.io.Resources


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var TAG ="JVVM"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = RetrofitServiceFactory.makeRetrofitService(this)

        val token = TokenManage(this)
        val textToken = token.getToken()

        //if(textToken.isNullOrBlank()){
        //    Toast.makeText(this,"No guardo el token",Toast.LENGTH_SHORT).show()
        //}else{
        //   Toast.makeText(this,"Guardo el token",Toast.LENGTH_SHORT).show()
        //}

        //Consulta normal
        lifecycleScope.launch {
            val listGames = service.listGames()
            runOnUiThread {
                //showGamesPruebas(listGames)
            }
        }
        val context = this
        //Con esto me permite realizar el login de la pagina (metodo post necesitan crear un modelo para poder enviarlo como un json, usar el serializableName para ello)
        lifecycleScope.launch {
            val objRequest = PostModelLogin(
                username = "PAQUITO",
                password = "PRUEBA")
            val response = service.signIn(objRequest)
            showToken(response)
            if(response.message == "OK"){
                runOnUiThread{
                    token.saveToken(response.token)
                    Almacen.token = response.token
                    val claims = GeneralFuntion.decodeJWT(Almacen.token)
                    if (claims != null) {
                        val username = claims["USERNAME"]?.toString()
                        val id_user = claims["ID_USUARIO"]
                        val id_rol = claims["ID_ROL"]?.toString()
                        // Obtener otros datos según los campos del token
                        Log.e(TAG,id_user.toString())
                        Log.e(TAG,id_rol.toString())
                        Log.e(TAG,username.toString())
                    } else {
                        Toast.makeText(context,"NO SE A PODIDO DECODIFICAR",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpPrincipal.adapter = AdapterViewPage(this)
        TabLayoutMediator(binding.tabLayout,binding.vpPrincipal){tab,index->
            tab.text = when(index){
                0-> ""
                else -> ""
            }
            tab.icon = when(index){
                0-> getDrawable(R.drawable.gamepad_solid)
                else -> {throw Resources.NotFoundException("Posicion no encontrada")}
            }

        }.attach()


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

    fun showGamesPruebas(games:Array<Game>){
        for (game in games){
            Toast.makeText(this,game.TITULO,Toast.LENGTH_SHORT).show()
        }
    }

    fun showToken(result: UserLogin){
        Toast.makeText(this,"'"+result.message+"'",Toast.LENGTH_LONG).show()
    }
}