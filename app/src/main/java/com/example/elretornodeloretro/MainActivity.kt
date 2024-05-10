package com.example.elretornodeloretro

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.elretornodeloretro.databinding.ActivityMainBinding
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.UserLogin
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import com.example.elretornodeloretro.io.FuncionesGenerales


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var TAG ="JVVM"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = RetrofitServiceFactory.makeRetrofitService(this)

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
                    //val textView = findViewById<TextView>(R.id.prueba)
                    Almacen.token = response.token
                    val claims = FuncionesGenerales.decodeJWT(Almacen.token)
                    if (claims != null) {
                        val username = claims["USERNAME"]?.toString()
                        val id_user = claims["ID_USUARIO"]
                        val id_rol = claims["ID_ROL"]?.toString()
                        // Obtener otros datos según los campos del token
                        Toast.makeText(context,id_rol,Toast.LENGTH_SHORT).show()
                        Log.e(TAG,id_user.toString())
                        //Toast.makeText(context,id_user,Toast.LENGTH_SHORT).show()
                        Toast.makeText(context,username,Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,"NO SE A PODIDO DECODIFICAR",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBorrar.setOnClickListener {
            deleteGame(binding.editTextText.text.toString().toInt())
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

    fun showGamesPruebas(games:Array<Game>){
        for (game in games){
            Toast.makeText(this,game.TITULO,Toast.LENGTH_SHORT).show()
        }
    }

    fun showToken(result: UserLogin){
        Toast.makeText(this,"'"+result.message+"'",Toast.LENGTH_LONG).show()
    }
}