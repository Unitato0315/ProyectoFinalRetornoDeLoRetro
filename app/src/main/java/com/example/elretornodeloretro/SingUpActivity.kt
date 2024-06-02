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
import com.example.elretornodeloretro.model.Almacen
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
                    Almacen.startSession = 1
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
        if(binding.edUserReg.text.toString().isNullOrBlank()){
            Toast.makeText(this,"No puedes dejar vacio el username",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.edEmailReg.text.toString().isNullOrBlank()){
            Toast.makeText(this,"No puedes dejar vacio el email",Toast.LENGTH_SHORT).show()
            return false
        }else{
            val emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
            if(!binding.edEmailReg.text.toString().matches(emailPattern.toRegex())){
                Toast.makeText(this,"Tienes que introducir un email valido",Toast.LENGTH_SHORT).show()
                return false
            }
        }
        if(binding.edPassReg.text.toString().isNullOrBlank()){
            Toast.makeText(this,"No puedes dejar vacia la contraseña",Toast.LENGTH_SHORT).show()
            return false
        }else{
            val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"
            if(!binding.edPassReg.text.toString().matches(passwordPattern.toRegex())){
                Toast.makeText(this,"La contraseña no es valida\n-8 Caracteres\n-1 Mayuscula y 1 Minuscula\n 1Caracter especial",Toast.LENGTH_LONG).show()
                return false
            }
        }
        if(binding.edConfReg.text.toString().isNullOrBlank()){
            Toast.makeText(this,"No puedes dejar vacio la confirmacion de contraseña",Toast.LENGTH_SHORT).show()
            return false
        }else if(binding.edConfReg.text.toString()!=binding.edPassReg.text.toString()){
            Toast.makeText(this,"No coinciden las contraseñas",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.edNombre.text.toString().isNullOrBlank()){
            Toast.makeText(this,"No puedes dejar vacio el nombre",Toast.LENGTH_SHORT).show()
            return false
        }
        if(binding.edApellido.text.toString().isNullOrBlank()){
            Toast.makeText(this,"No puedes dejar vacio los apellidos",Toast.LENGTH_SHORT).show()
            return false

        }
        if(binding.edDni.text.toString().isNullOrBlank()){
            Toast.makeText(this,"No puedes dejar vacio el DNI",Toast.LENGTH_SHORT).show()
            return false
        }else{
            val dniNie: String = binding.edDni.text.toString()
            val dniPattern = Regex("^[0-9]{8}[A-HJ-NP-TV-Z]$")
            val niePattern = Regex("^[XYZ][0-9]{7}[A-Z]$")

            if (!dniNie.matches(dniPattern) && !dniNie.matches(niePattern)) {
                Toast.makeText(this,"No es un dni valido",Toast.LENGTH_SHORT).show()
                return false
            }
        }
        if(!binding.cbCondiciones.isChecked){
            Toast.makeText(this,"No has aceptado los terminos y condiciones",Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}