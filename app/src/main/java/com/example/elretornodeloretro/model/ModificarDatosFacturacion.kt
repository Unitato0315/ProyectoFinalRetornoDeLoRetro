package com.example.elretornodeloretro.model

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.adapter.AdapterPedidos
import com.example.elretornodeloretro.databinding.ActivityModificarDatosFacturacionBinding
import com.example.elretornodeloretro.databinding.ActivityPedidosBinding
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import kotlinx.coroutines.launch

class ModificarDatosFacturacion : AppCompatActivity() {
    lateinit var binding: ActivityModificarDatosFacturacionBinding
    lateinit var service: ServiceRetrofit
    lateinit var tokenManage: TokenManage
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarDatosFacturacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        service = RetrofitServiceFactory.makeRetrofitService(this)
        tokenManage = TokenManage(this)
        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
        val idUsuario = claim!!["ID_USUARIO"].toString().toInt()

        lifecycleScope.launch {
            val userData = service.getUser(idUsuario)[0]
            runOnUiThread {
                binding.edDireccionModificar.setText(userData.DIRECCION)
                binding.edLocalidadModificar.setText(userData.LOCALIDAD)
                binding.edCPModificar.setText(userData.CODIGO_POSTAL.toString())
                binding.edProvinciaModificar.setText(userData.PROVINCIA)
                binding.edTelefonoModificar.setText(userData.TELEFONO.toString())
            }
        }

        binding.tbModificarDatos.title = "MODIFICAR DATOS"
        setSupportActionBar(binding.tbModificarDatos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbModificarDatos.setNavigationOnClickListener {
            finish()
        }

        binding.btnModificar.setOnClickListener {
            lifecycleScope.launch {
                val userData = UserDataModify(
                    cp = binding.edCPModificar.text.toString().toInt(),
                    direccion = binding.edDireccionModificar.text.toString(),
                    localidad = binding.edLocalidadModificar.text.toString(),
                    provicia = binding.edProvinciaModificar.text.toString(),
                    telefono =  binding.edTelefonoModificar.text.toString().toInt()
                )
                service.setDatosUser(idUsuario,userData)
                runOnUiThread {
                    Toast.makeText(context,"Se ha modificado correctamente",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }
}