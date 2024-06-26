package com.example.elretornodeloretro

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterResumeBuy
import com.example.elretornodeloretro.databinding.ActivityPayBinding
import com.example.elretornodeloretro.io.EmailSender
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.InformationOrder
import com.example.elretornodeloretro.model.ResponseOrder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class PayActivity : AppCompatActivity() {
    lateinit var binding: ActivityPayBinding
    lateinit var adapter: AdapterResumeBuy
    lateinit var recycler: RecyclerView
    lateinit var service: ServiceRetrofit
    lateinit var context: Context
    lateinit var tokenManage: TokenManage
    var nombre = ""
    var email = ""
    lateinit var loadingDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPayBinding.inflate(layoutInflater)

        setSupportActionBar(binding.tbMenuResumen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbMenuResumen.title = "RESUMEN"
        binding.tbMenuResumen.setNavigationOnClickListener {
            finish()
        }
        context = this
        service = RetrofitServiceFactory.makeRetrofitService(this)
        tokenManage = TokenManage(this)
        recycler = binding.rvResumen
        recycler.setHasFixedSize(true)
        recycler.layoutManager = GridLayoutManager(this,1)
        adapter = AdapterResumeBuy(Almacen.cart,this)
        recycler.adapter = adapter

        var paysString : MutableList<String> = mutableListOf()

        for (pay in Almacen.tipesPays){
            paysString.add(pay.FORMA_DE_PAGO)
        }
        var selectecPays = 0
        var selectecSend = 0
        val adapterPays = ArrayAdapter(this,android.R.layout.simple_spinner_item,paysString)
        binding.sPagos.adapter = adapterPays

        binding.sPagos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectecPays = position
                if(selectecPays == 3){
                    binding.sEnvios.setSelection(7)
                    selectecSend = 7
                    binding.sEnvios.isEnabled = false
                }else{
                    binding.sEnvios.isEnabled = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        var sendString : MutableList<String> = mutableListOf()

        for (send in Almacen.tipesSend){
            sendString.add(send.TIPO_ENVIO)
        }

        val adapterSend = ArrayAdapter(this,android.R.layout.simple_spinner_item,sendString)
        binding.sEnvios.adapter = adapterSend

        binding.sEnvios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectecSend = position


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.btnContinuar.text  ="Total: ${String.format("%.2f",Almacen.totalPrice)}€"

        binding.btnContinuar.setOnClickListener {
            validar(selectecSend,selectecPays)
        }

        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
        val idUsuario = claim!!["ID_USUARIO"].toString().toInt()

        lifecycleScope.launch {
            val userData = service.getUser(idUsuario)[0]
            runOnUiThread {
                binding.edDireccion.setText(userData.DIRECCION)
                binding.edLocalidad.setText(userData.LOCALIDAD)
                binding.edCP.setText(userData.CODIGO_POSTAL.toString())
                binding.edProvincia.setText(userData.PROVINCIA)
                binding.edTelefono.setText(userData.TELEFONO.toString())
                email = userData.EMAIL
                nombre = userData.NOMBRE+" "+userData.APELLIDO
            }
        }

        setContentView(binding.root)
    }
    fun validar(send: Int, pay:Int){
        if(send !=0 && pay != 0){
            if(pay == 2){
                pagoTarjeta(send,pay)
            }else{
                finalizar(send,pay,0,0,0)
            }
        }else{
            Toast.makeText(this,"Selecciona el metodo de pago y de envio",Toast.LENGTH_LONG).show()
        }
    }
    fun pagoTarjeta(send: Int, pay:Int){
        var cardNumber: Long
        var cvv = 0
        var expired = 0
        var builder = MaterialAlertDialogBuilder(context)
        val inflater = layoutInflater
        builder.setTitle("")
        val dialogLayout = inflater.inflate(R.layout.dialog_card_pay,null)
        val buttonPay: Button = dialogLayout.findViewById(R.id.btnPagarTarjeta)
        val labelCardNumber: EditText = dialogLayout.findViewById(R.id.edCardNumber)
        val labelCVV: EditText = dialogLayout.findViewById(R.id.edCVV)
        val labelExpired: EditText = dialogLayout.findViewById(R.id.edExpired)
        val labelName: EditText = dialogLayout.findViewById(R.id.edNameCard)

        buttonPay.setOnClickListener {
            if(!labelName.text.toString().isNullOrBlank()&&!labelExpired.text.toString().isNullOrBlank()&&!labelCVV.text.toString().isNullOrBlank()&&!labelCardNumber.text.toString().isNullOrBlank()){
                cardNumber = labelCardNumber.text.toString().toLong()
                cvv = labelCVV.text.toString().toInt()
                expired = labelExpired.text.toString().toInt()
                var validar = true

                if(cardNumber < 1000000000000000){
                    Toast.makeText(context, "El numero de la tarjeta no es correcto", Toast.LENGTH_SHORT).show()
                    validar=false
                }
                val mes = expired.toString().substring(0,2)
                if(mes.toInt() > 12){
                    Toast.makeText(context, "Se ha introducido una fecha invalida", Toast.LENGTH_SHORT).show()
                    validar=false
                }

                if(validar){
                    finalizar(send,pay,cardNumber,cvv,expired)
                }
            }else{
                Toast.makeText(context, "No se han introducido todos los datos", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setView(dialogLayout)
        builder.show()

        Toast.makeText(this,"Selecciona el metodo con tarjeta",Toast.LENGTH_LONG).show()
    }

    fun finalizar(send: Int, pay:Int,carNumber:Long,cvv:Int,expiredDate:Int){
        var tokenManage = TokenManage(context)

        var listId :MutableList<Int> = mutableListOf()
        var token = tokenManage.getToken()

        var claim = GeneralFuntion.decodeJWT(token!!)

        for (game in Almacen.cart){
            listId.add(game.ID_PRODUCTO)
        }
        val informationOrder = InformationOrder(
            cardNumber =  carNumber,
            cp = binding.edCP.text.toString().toInt(),
            cvv = cvv,
            direccion = binding.edDireccion.text.toString(),
            expiryDate = expiredDate,
            idUsuario = claim?.get("ID_USUARIO").toString().toInt(),
            localidad = binding.edLocalidad.text.toString(),
            productId = listId,
            provincia = binding.edProvincia.text.toString(),
            tipoEnvio = Almacen.tipesSend[send].ID_ENVIO,
            tipoPago = Almacen.tipesPays[pay].ID_PAGO,
            total = Almacen.totalPrice
        )
        lifecycleScope.launch {
            showLoadingDialog()
            val response: ResponseOrder = service.createOrder(informationOrder)
            runOnUiThread {
                if(response.success){

                    Toast.makeText(context,response.message,Toast.LENGTH_LONG).show()
                    lifecycleScope.launch {
                        try {
                            val to = email
                            val subject = "Confirmacion pedido Nº ${response.idPedido}"
                            var bodyText = """
                                ¡Hola ${nombre}!
                                
                                Gracias por comprar en el Retorno de lo Retro.
                                Nos alegra informarte que hemos recibido tu pedido y estamos trabajando para procesarlo lo antes posible. 
                                Aquí tienes los detalles de tu pedido:
                                
                                Numero pedido: ${response.idPedido}
                                
                                Productos:
                                
                            """.trimIndent()
                            for(producto in Almacen.cart){
                                bodyText += "       ${producto.TITULO}: ${String.format("%.2f", producto.PRECIO_FINAL)}€\n"
                            }
                            bodyText += """
                                  Total: ${String.format("%.2f", Almacen.totalPrice)}€
                                  
                                 ¡Gracias por confiar en nosotros!

                                 Atentamente,
                                        El equipo de El Retorno de lo Retro
                            """.trimIndent()

                            EmailSender.sendEmail(to, subject, bodyText)
                            runOnUiThread {
                                Almacen.cart = mutableListOf()
                                Almacen.totalPrice = 0f
                                loadingDialog.dismiss()
                                finish()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Almacen.cart = mutableListOf()
                            Almacen.totalPrice = 0f
                            loadingDialog.dismiss()
                            finish()
                        }
                    }

                }else{
                    Toast.makeText(context,response.message,Toast.LENGTH_LONG).show()
                    if(response.productos.isNotEmpty()){
                        Almacen.cart = mutableListOf()
                        Almacen.totalPrice = 0f
                        loadingDialog.dismiss()
                        finish()
                    }
                }
            }
        }

    }
    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(context,R.style.CustomDialog)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_refrest, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.show()
    }

}