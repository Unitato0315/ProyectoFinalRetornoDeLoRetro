package com.example.elretornodeloretro

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.elretornodeloretro.adapter.AdapterViewPage
import com.example.elretornodeloretro.adapter.AdapterViewPageAdmin
import com.example.elretornodeloretro.adapter.AdapterViewPageNormalUser
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

        val context = this
        // Me traigo por primera vez los datos necesarios, para mostrar los productos(se actualizan) y distintos spinner que no deben cambiar
        lifecycleScope.launch {
            val listGames = service.listGames()
            val tipesSends = service.getTipeSend()
            val tipesPays = service.getTipePay()
            val tipos = service.getTipos()
            val plataformas = service.getPlataforma()
            runOnUiThread {
                //showGamesPruebas(listGames)
                Almacen.games = listGames
                Almacen.tipesSend = tipesSends
                Almacen.tipesPays = tipesPays
                Almacen.plataformas = plataformas.toMutableList()
                Almacen.tipos = tipos.toMutableList()
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                //Se obtiene el token para comprobar el tipo de usuario
                var token2 = tokenManage.getToken()

                if(!token2.isNullOrBlank()){
                    //Si existe token se decodifica si esta caducado se elimina
                    val claims = GeneralFuntion.decodeJWT(token2!!)
                    if (claims == null){
                        tokenManage.deleteToken()
                        token2 = ""
                    }
                }
                // se comprueba el rol del usuario y se dibuja una interfaz para cada tipo
                if(token2.isNullOrBlank()){
                    binding.vpPrincipal.adapter = AdapterViewPage(context)
                    TabLayoutMediator(binding.tabLayout,binding.vpPrincipal){tab,index->
                        tab.text = when(index){
                            0-> ""
                            1-> ""
                            else -> ""
                        }
                        tab.icon = when(index){
                            0-> getDrawable(R.drawable.gamepad_solid)
                            1-> getDrawable(R.drawable.user_check_solid)
                            else -> {throw Resources.NotFoundException("Posicion no encontrada")}
                        }
                    }.attach()
                }else{
                    val claims = GeneralFuntion.decodeJWT(token2)
                    if(claims != null){
                        val id_rol = claims["ID_ROL"]?.toString()
                        if (id_rol == "99"){
                            binding.vpPrincipal.adapter = AdapterViewPageAdmin(context)
                            TabLayoutMediator(binding.tabLayout,binding.vpPrincipal){tab,index->
                                tab.text = when(index){
                                    0-> ""
                                    1-> ""
                                    2-> ""
                                    else -> ""
                                }
                                tab.icon = when(index){
                                    0-> getDrawable(R.drawable.gamepad_solid)
                                    1-> getDrawable(R.drawable.message_solid)
                                    2-> getDrawable(R.drawable.user_solid)
                                    else -> {throw Resources.NotFoundException("Posicion no encontrada")}
                                }
                            }.attach()
                        }else{
                            binding.vpPrincipal.adapter = AdapterViewPageNormalUser(context)
                            TabLayoutMediator(binding.tabLayout,binding.vpPrincipal){tab,index->
                                tab.text = when(index){
                                    0-> ""
                                    1-> ""
                                    2-> ""
                                    3-> ""
                                    else -> ""
                                }
                                tab.icon = when(index){
                                    0-> getDrawable(R.drawable.gamepad_solid)
                                    1-> getDrawable(R.drawable.cart_shopping_solid)
                                    2-> getDrawable(R.drawable.message_solid)
                                    3-> getDrawable(R.drawable.user_solid)
                                    else -> {throw Resources.NotFoundException("Posicion no encontrada")}
                                }
                            }.attach()
                        }
                    }
                }
            }
        }

    }

    override fun onRestart() {
        super.onRestart()
        //compruebo si hay un token y si se acaba de iniciar sesion
        if(tokenManage.getToken()!="" && Almacen.startSession != 0){
            Almacen.startSession = 0
            recreate()
        }

    }
}