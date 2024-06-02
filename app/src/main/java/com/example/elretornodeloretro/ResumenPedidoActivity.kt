package com.example.elretornodeloretro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterResumePedido
import com.example.elretornodeloretro.databinding.ActivityResumenPedidoBinding
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Estado
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.Locale

class ResumenPedidoActivity : AppCompatActivity() {
    lateinit var binding: ActivityResumenPedidoBinding
    lateinit var tokenManage: TokenManage
    lateinit var adapterResumenPedido: AdapterResumePedido
    lateinit var recyclerResume: RecyclerView
    lateinit var service: ServiceRetrofit
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        tokenManage = TokenManage(this)
        service = RetrofitServiceFactory.makeRetrofitService(this)
        binding.tbResumen.title = "Nº "+ Almacen.selectecPedido.ID_PEDIDO
        setSupportActionBar(binding.tbResumen)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tbResumen.setNavigationOnClickListener {
            finish()
        }

        recyclerResume = binding.rvResumenPedido
        recyclerResume.setHasFixedSize(true)
        recyclerResume.layoutManager = GridLayoutManager(context,1)

        binding.edTelefonoResumen.setText(Almacen.selectecPedido.TELEFONO.toString())
        binding.edLocalidadResumen.setText(Almacen.selectecPedido.LOCALIDAD)
        binding.edProvinciaResumen.setText(Almacen.selectecPedido.PROVINCIA)
        binding.edDireccionResumen.setText(Almacen.selectecPedido.DIRECCION)
        binding.edTipoEnvio.setText(Almacen.selectecPedido.TIPO_ENVIO)
        binding.edTipoPago.setText(Almacen.selectecPedido.FORMA_DE_PAGO)
        binding.edCPResumen.setText(Almacen.selectecPedido.CP.toString())


        lifecycleScope.launch {
            var listProductOrder = service.getProductOrder(Almacen.selectecPedido.ID_PEDIDO)
            runOnUiThread {
                adapterResumenPedido = AdapterResumePedido(listProductOrder.toMutableList(), context)
                recyclerResume.adapter = adapterResumenPedido
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_resumen_pedido, menu)
        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
        if(claim!!["ID_ROL"].toString().toInt() == 99){
            menu?.findItem(R.id.opcion_reembolso)?.isVisible = false
            when (Almacen.selectecPedido.ID_ESTADO_PEDIDO) {
                1 -> {
                    menu?.findItem(R.id.opcion_entregado)?.isVisible = false
                }
                2 -> {
                    menu?.findItem(R.id.opcion_enviar)?.isVisible = false
                }
                else -> {
                    menu?.findItem(R.id.opcion_enviar)?.isVisible = false
                    menu?.findItem(R.id.opcion_entregado)?.isVisible = false
                    menu?.findItem(R.id.opcion_cancelar)?.isVisible = false
                }
            }
        }else{
            menu?.findItem(R.id.opcion_enviar)?.isVisible = false
            menu?.findItem(R.id.opcion_entregado)?.isVisible = false
            if(Almacen.selectecPedido.ID_ESTADO_PEDIDO == 4 || Almacen.selectecPedido.ID_ESTADO_PEDIDO == 5){
                menu?.findItem(R.id.opcion_reembolso)?.isVisible = false
                menu?.findItem(R.id.opcion_cancelar)?.isVisible = false
            }else if(Almacen.selectecPedido.ID_ESTADO_PEDIDO == 3){
                menu?.findItem(R.id.opcion_cancelar)?.isVisible = false
            }
        }

        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.opcion_enviar -> {
                cambiarEstado(2)
            }
            R.id.opcion_cancelar -> {
                cambiarEstado(5)
            }
            R.id.opcion_entregado -> {
                cambiarEstado(3)
            }
            R.id.opcion_reembolso ->{
                cambiarEstado(4)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun cambiarEstado(estado:Int){
        Almacen.selectecPedido.ID_ESTADO_PEDIDO = estado
        lifecycleScope.launch {
            service.setEstado(Almacen.selectecPedido.ID_PEDIDO,Estado(estado))
            runOnUiThread {
                recreate()
            }
        }
    }
}