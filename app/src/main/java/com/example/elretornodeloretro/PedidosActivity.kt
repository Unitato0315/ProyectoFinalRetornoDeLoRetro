package com.example.elretornodeloretro

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterPedidos
import com.example.elretornodeloretro.databinding.ActivityPedidosBinding
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Order
import kotlinx.coroutines.launch

class PedidosActivity : AppCompatActivity() {
    lateinit var binding: ActivityPedidosBinding
    lateinit var adapterPedidos: AdapterPedidos
    lateinit var recycler: RecyclerView
    lateinit var service: ServiceRetrofit
    lateinit var tokenManage: TokenManage
    lateinit var context: Context
    var claim: Map<String,Any>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        service = RetrofitServiceFactory.makeRetrofitService(this)
        tokenManage = TokenManage(this)
        recycler = binding.rvPedidos
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(this,1)
        claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
        binding.tbPedidos.title = "PEDIDOS"
        setSupportActionBar(binding.tbPedidos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tbPedidos.setNavigationOnClickListener {
            finish()
        }
        binding.tbPedidos.title = "PEDIDOS"
        cargarPedidos()

    }

    fun cargarPedidos(){
        lifecycleScope.launch {
            var listPedidos: MutableList<Order> = mutableListOf()
            listPedidos = if(claim!!["ID_ROL"].toString().toInt()==99) {
                service.getAllOrder().toMutableList()

            }else{
                service.getOrder(claim!!["ID_USUARIO"].toString().toInt()).toMutableList()
            }
            runOnUiThread{
                if(listPedidos.size == 0){
                    binding.textView2.visibility = View.VISIBLE
                }else{
                    binding.textView2.visibility = View.INVISIBLE
                }
                adapterPedidos = AdapterPedidos(listPedidos,context)
                recycler.adapter = adapterPedidos
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cargarPedidos()
    }
}