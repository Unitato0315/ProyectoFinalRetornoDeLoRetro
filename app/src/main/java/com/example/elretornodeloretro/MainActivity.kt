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

        //tokenManage.deleteToken()

        val context = this
         //       username = "PAQUITO",
         //       password = "PRUEBA")
        lifecycleScope.launch {
            val listGames = service.listGames()
            val tipesSends = service.getTipeSend()
            val tipesPays = service.getTipePay()
            runOnUiThread {
                //showGamesPruebas(listGames)
                Almacen.games = listGames
                Almacen.tipesSend = tipesSends
                Almacen.tipesPays = tipesPays
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

                var token2 = tokenManage.getToken()

                if(!token2.isNullOrBlank()){
                    val claims = GeneralFuntion.decodeJWT(token2!!)
                    if (claims == null){
                        tokenManage.deleteToken()
                        token2 = ""
                    }
                }

                //Toast.makeText(context,token2.isNullOrBlank().toString(),Toast.LENGTH_SHORT).show()
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
                    //Toast.makeText(context,claims.toString(),Toast.LENGTH_SHORT).show()
                    //Log.e(TAG,claims.toString())
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
    //Como referencia
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

    override fun onRestart() {
        super.onRestart()
        if(tokenManage.getToken()!=""){
            recreate()
        }

    }
}