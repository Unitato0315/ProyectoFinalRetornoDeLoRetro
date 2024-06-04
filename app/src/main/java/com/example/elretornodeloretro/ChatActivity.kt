package com.example.elretornodeloretro

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterMesageChatCard
import com.example.elretornodeloretro.databinding.ActivityChatBinding
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.EnviarMensaje
import com.example.elretornodeloretro.model.MensajeChat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var adapterChat: AdapterMesageChatCard
    lateinit var recycler: RecyclerView
    lateinit var service: ServiceRetrofit
    lateinit var context: Context
    lateinit var tokenManage: TokenManage
    var tamanioLista = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        service = RetrofitServiceFactory.makeRetrofitService(this)
        tokenManage = TokenManage(this)
        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken()!!)

        binding.tbChat.title = Almacen.selectecChat.TITULO
        if(claim!!["ID_ROL"].toString().toInt() == 99){
            binding.tbChat.subtitle= Almacen.selectecChat.NOMBRE+" "+Almacen.selectecChat.APELLIDO
        }
        setSupportActionBar(binding.tbChat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbChat.setNavigationOnClickListener {
            finish()
        }

        recycler = binding.rcChat
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(this,1)

        binding.imgMandar.setOnClickListener {
            enviarMensaje(claim!!["ID_ROL"].toString().toInt())
        }

        obtenerMensajes()
        obtenerMensajesTemporizador()
    }
    fun obtenerMensajes(){
        lifecycleScope.launch {
            var listMensajes: MutableList<MensajeChat> = service.obtenerMensajes(Almacen.selectecChat.ID_CHAT).toMutableList()
            runOnUiThread {
                adapterChat = AdapterMesageChatCard(listMensajes,context)
                recycler.adapter = adapterChat
                if(tamanioLista < listMensajes.size){
                    recycler.scrollToPosition(listMensajes.size-1)
                }

            }
        }
    }

    fun enviarMensaje(rol: Int) {
        lifecycleScope.launch {
            var admin = 0
            if(rol == 99){
                admin = 1
            }
            val objeto = EnviarMensaje(
                mensaje = binding.editTextTextMultiLine.text.toString(),
                admin = admin
            )
            service.crearMensaje(Almacen.selectecChat.ID_CHAT,objeto)
            runOnUiThread {
                binding.editTextTextMultiLine.setText("")
                obtenerMensajes()
            }
        }
    }

    fun obtenerMensajesTemporizador(){
        lifecycleScope.launch {
            while (true){
                delay(2000)
                obtenerMensajes()
            }
        }
    }
}