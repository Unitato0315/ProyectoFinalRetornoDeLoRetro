package com.example.elretornodeloretro

import android.os.Bundle
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
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.UserLogin
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = RetrofitServiceFactory.makeRetrofitService()

        //Consulta normal
        lifecycleScope.launch {
            val listGames = service.listGames()
            runOnUiThread {
                //showGamesPruebas(listGames)
            }
        }

        //Con esto me permite realizar el login de la pagina (metodo post necesitan crear un modelo para poder enviarlo como un json, usar el serializableName para ello)
        lifecycleScope.launch {
            val objRequest = PostModelLogin(
                username = "PAQUITO",
                password = "PRUEBA")
            val response = service.signIn(objRequest)
            showToken(response)
            if(response.message == "OK"){
                runOnUiThread{
                    val textView = findViewById<TextView>(R.id.prueba)
                    textView.text = response.token
                }
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBorrar.setOnClickListener {
            //deleteGame(binding.editTextText.text.toString().toInt(),"")
        }

    }

    private fun deleteGame(id: Int,token: String) {
        val content = this
        lifecycleScope.launch {
            val request = RetrofitServiceFactory.makeRetrofitService().deleteGame(id,token)
            if(request.text == "El juego fue eliminado"){
                Toast.makeText(content,"Se ha eliminado correctamente",Toast.LENGTH_LONG).show()
            }
        }
    }


    fun decodeJWT(token: String): Map<String, Any>? {
        try {
            val algorithm = Algorithm.none() // Puedes usar un algoritmo adecuado según cómo se firmó el token
            val verifier = JWT.require(algorithm).build()
            val jwt = verifier.verify(token)
            return jwt.claims
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
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