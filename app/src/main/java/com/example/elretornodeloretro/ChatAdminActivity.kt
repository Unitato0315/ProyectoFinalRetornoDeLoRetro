package com.example.elretornodeloretro

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterMesageChatCard
import com.example.elretornodeloretro.adapter.AdapterMesageChatCardAdmin
import com.example.elretornodeloretro.databinding.ActivityChatAdminBinding
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

class ChatAdminActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatAdminBinding
    lateinit var adapterChat: AdapterMesageChatCardAdmin
    lateinit var recycler: RecyclerView
    lateinit var service: ServiceRetrofit
    lateinit var context: Context
    lateinit var tokenManage: TokenManage
    var tamanioLista = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        service = RetrofitServiceFactory.makeRetrofitService(this)
        tokenManage = TokenManage(this)
        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken()!!)

        binding.tbChatAdmin.title = Almacen.selectecChat.TITULO
        if(claim!!["ID_ROL"].toString().toInt() == 99){
            binding.tbChatAdmin.subtitle= Almacen.selectecChat.NOMBRE+" "+ Almacen.selectecChat.APELLIDO
        }
        setSupportActionBar(binding.tbChatAdmin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbChatAdmin.setNavigationOnClickListener {
            finish()
        }

        recycler = binding.rcChatAdmin
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(this,1)

        binding.imgMandarAdmin.setOnClickListener {
            if(!binding.editTextTextMultiLineAdmin.text.isNullOrBlank()){
                enviarMensaje(claim!!["ID_ROL"].toString().toInt())
            }
        }

        obtenerMensajes()
        obtenerMensajesTemporizador()
    }
    //Recupero todos los mensajes actuales de la base de datos
    fun obtenerMensajes(){
        lifecycleScope.launch {
            var listMensajes: MutableList<MensajeChat> = service.obtenerMensajes(Almacen.selectecChat.ID_CHAT).toMutableList()
            runOnUiThread {
                if(tamanioLista < listMensajes.size){
                    adapterChat = AdapterMesageChatCardAdmin(listMensajes,context)
                    recycler.adapter = adapterChat
                    recycler.scrollToPosition(listMensajes.size-1)
                    tamanioLista = listMensajes.size
                }

            }
        }
    }
    //Crea los mensajes en la base de datos
    fun enviarMensaje(rol: Int) {
        lifecycleScope.launch {
            var admin = 0
            if(rol == 99){
                admin = 1
            }
            val objeto = EnviarMensaje(
                mensaje = binding.editTextTextMultiLineAdmin.text.toString(),
                admin = admin
            )
            service.crearMensaje(Almacen.selectecChat.ID_CHAT,objeto)
            runOnUiThread {
                binding.editTextTextMultiLineAdmin.setText("")
                obtenerMensajes()
            }
        }
    }
    //Bucle encargado de mantener actualizados los mensajes
    fun obtenerMensajesTemporizador(){
        lifecycleScope.launch {
            while (true){
                delay(2000)
                obtenerMensajes()
            }
        }
    }
}