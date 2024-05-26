package com.example.elretornodeloretro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.elretornodeloretro.databinding.ActivitySingUpBinding
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SingUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySingUpBinding
    var msg :String = ""
    lateinit var serviceRetrofit: ServiceRetrofit
    lateinit var tokenManage: TokenManage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        serviceRetrofit = RetrofitServiceFactory.makeRetrofitService(this)
        tokenManage = TokenManage(this)
        setContentView(binding.root)
        setSupportActionBar(binding.tbRegistro)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tbRegistro.setNavigationOnClickListener {
            finish()
        }
        binding.btnRegistr.setOnClickListener {
            if(validar()){
                createUser(this)
            }else{
                showDialog(this)
            }
        }
    }

    fun createUser(context: Context){
        lifecycleScope.launch {
            val objRequest = User(
                APELLIDO = "",
                CODIGO_POSTAL = 0,
                DIRECCION = "",
                DNI = binding.edDni.text.toString(),
                EMAIL = binding.edEmailReg.text.toString(),
                ID_ROL = 1,
                ID_USUARIO = 0,
                LOCALIDAD = "",
                NOMBRE = binding.edNombre.text.toString(),
                PASSWORD = binding.edPassReg.text.toString(),
                PROVINCIA = "",
                TELEFONO = 0,
                USERNAME = binding.edUserReg.text.toString()
            )
            try{
                val response = serviceRetrofit.signUp(objRequest)
                runOnUiThread {
                    if(response.code()==409){
                        Toast.makeText(context,"Usuario ya existe", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"Se ha registrado correctamente", Toast.LENGTH_SHORT).show()
                        signUp(context)
                    }
                }
            }catch (e: HttpException){
                if(e.code()==409){
                    Toast.makeText(context,"Se ha producido un error", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Se ha producido un error", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(context,"Fallo en la solicitud", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun showDialog(context: Context){
        MaterialAlertDialogBuilder(context)
            .setTitle("Se ha producido un error")
            .setMessage(msg)
            .setPositiveButton("Ok"){dialog, which ->
            }
            .create()
            .show()
    }
    fun signUp(context: Context){
        lifecycleScope.launch {
            val objRequest2 = PostModelLogin(
                username = binding.edUserReg.text.toString(),
                password = binding.edPassReg.text.toString()
            )
            val request = serviceRetrofit.signIn(objRequest2)
            try {
                runOnUiThread {
                    tokenManage.saveToken(request.token)
                    Toast.makeText(context,"Se ha iniciado sesion correctamente",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }catch (e: HttpException){
                if(e.code()==400){
                    Toast.makeText(context,"Se ha producido un error", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Se ha producido un error", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(context,"Fallo en la solicitud", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun validar(): Boolean{
        var validate: Boolean = true
        msg = ""
        if(binding.edUserReg.text.toString().isNullOrBlank()){
            msg += "Tienes que introducir un usuario\n"
            validate = false
        }
        if(binding.edEmailReg.text.toString().isNullOrBlank()){
            msg += "Tienes que introducir un email\n"
            validate = false
        }
        if(binding.edPassReg.text.toString().isNullOrBlank()){
            msg += "Tienes que introducir una contraseña\n"
            validate = false
        }
        if(binding.edConfReg.text.toString().isNullOrBlank()){
            msg += "Tienes que confirmar la contraseña\n"
            validate = false
        }else if(binding.edConfReg.text.toString()!=binding.edPassReg.text.toString()){
            validate = false
        }
        if(binding.edNombre.text.toString().isNullOrBlank()){
            msg += "Tienes que introducir el nombre\n"
            validate = false
        }
        if(binding.edApellido.text.toString().isNullOrBlank()){
            msg += "Tienes que introducido el apellido\n"
            validate = false

        }
        if(binding.edDni.text.toString().isNullOrBlank()){
            validate = false
            msg += "Tienes que introducir un DNI valido\n"
        }else{
            val dniNie: String = binding.edDni.text.toString()
            val dniPattern = Regex("^[0-9]{8}[A-HJ-NP-TV-Z]$")
            val niePattern = Regex("^[XYZ][0-9]{7}[A-Z]$")

            if (!dniNie.matches(dniPattern) && !dniNie.matches(niePattern)) {
                msg += "Se ha introducido mal el dni\n"
                validate = false
            }
        }
        if(!binding.cbCondiciones.isChecked){
            msg += "Tienes que aceptar los terminos y condiciones\n"
            validate = false
        }

        return validate
    }
}