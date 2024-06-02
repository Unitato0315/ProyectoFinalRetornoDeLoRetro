package com.example.elretornodeloretro

import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.example.elretornodeloretro.databinding.ActivityCrearModificarProductoBinding
import com.example.elretornodeloretro.databinding.ActivityModificarDatosFacturacionBinding
import com.example.elretornodeloretro.model.Almacen
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Plataforma
import com.example.elretornodeloretro.model.ProductModiCreate
import com.example.elretornodeloretro.model.Tipo
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.util.UUID

class CrearModificarProducto : AppCompatActivity() {
    lateinit var binding: ActivityCrearModificarProductoBinding
    private val REQUEST_READ_EXTERNAL_STORAGE = 100
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference
    lateinit var context: Context
    var modificar = false
    var seleccionadaTipo: Int = 0
    var seleccionadaPlataforma: Int = 0
    lateinit var serviceRetrofit: ServiceRetrofit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearModificarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceRetrofit = RetrofitServiceFactory.makeRetrofitService(this)
        var storage = Firebase.storage

        var storageRef = storage.reference
        storageReference = FirebaseStorage.getInstance().reference.child("productos")
        context = this
        val intent = intent
        modificar = intent.getBooleanExtra("modificar",false)

        val adapterTipo = ArrayAdapter(this, android.R.layout.simple_spinner_item,Almacen.tipos)
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sTipos.adapter = adapterTipo
        binding.sTipos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedTipo = parent.getItemAtPosition(position) as Tipo
                seleccionadaTipo = selectedTipo.ID_TIPO
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }
        val adapterPlataforma = ArrayAdapter(this, android.R.layout.simple_spinner_item,Almacen.plataformas)
        adapterPlataforma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sPlataforma.adapter = adapterPlataforma
        binding.sPlataforma.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedPlataforma = parent.getItemAtPosition(position) as Plataforma
                seleccionadaPlataforma = selectedPlataforma.ID_PLATAFORMA
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }

        if(modificar){
            binding.tbGameCreation.title = "Modificar"
            binding.edPrecioCreacOn.setText(Almacen.selectecGame.PRECIO_FINAL.toString())
            binding.edTituloCreacion.setText(Almacen.selectecGame.TITULO)
            binding.edDescriptionCreacion.setText(Almacen.selectecGame.DESCRIPCION)
            binding.btnModCre.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.md_theme_primaryFixedDim, null))
            binding.btnModCre.icon = ContextCompat.getDrawable(this,R.drawable.pen_to_square_solid)
            binding.btnModCre.compoundDrawablePadding = 8
            binding.btnModCre.text = "Modificar"
            selectSpinnerTipo(Almacen.selectecGame.ID_TIPO)
            selectSpinnerPlataforma(Almacen.selectecGame.ID_PLATAFORMA)
            var spaceRef = storageRef.child("productos/${Almacen.selectecGame.IMAGEN}.jpg")
            val localfile  = File.createTempFile("tempImage","jpg")
            spaceRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                val scaleBitmap = Bitmap.createScaledBitmap(bitmap,500,500,true)
                binding.imGameCreacion.setImageBitmap(scaleBitmap)
            }.addOnFailureListener{
                Toast.makeText(this,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
            }
        }else{
            binding.tbGameCreation.title = "Crear"
        }
        setSupportActionBar(binding.tbGameCreation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbGameCreation.setNavigationOnClickListener {
            finish()
        }

        binding.imGameCreacion.setOnClickListener{
            openGallery()
        }

        binding.btnModCre.setOnClickListener {
            if(seleccionadaTipo != 0 && seleccionadaPlataforma != 0 && !binding.edTituloCreacion.text.toString().isNullOrBlank() && !binding.edDescriptionCreacion.text.toString().isNullOrBlank() && !binding.edPrecioCreacOn.text.toString().isNullOrBlank()){
                if(modificar){
                    modificarGame()
                }else{
                    if(imageUri != null){
                        createGame()
                    }else{
                        Toast.makeText(this,"No se ha seleccionado una foto",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"No se han rellenado todos los campos",Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_EXTERNAL_STORAGE)
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            if (imageUri!= null) {
                binding.imGameCreacion.setImageURI(imageUri)
            }
        }
    }

    private fun uploadImageToFirebase(fileName: String) {
        if (imageUri != null) {
            val fileReference = storageReference.child(fileName)

            fileReference.putFile(imageUri!!)
                .addOnSuccessListener {
                    Toast.makeText(this, "Imagen subida con éxito", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                }.addOnCompleteListener{
                    finish()
                }
        } else {
            Toast.makeText(this, "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show()
        }

    }
    private fun selectSpinnerTipo(id: Int) {
        val adapter = binding.sTipos.adapter as ArrayAdapter<Tipo>
        for (position in 0 until adapter.count) {
            if (adapter.getItem(position)?.ID_TIPO == id) {
                binding.sTipos.setSelection(position)
                break
            }
        }
    }

    private fun selectSpinnerPlataforma(id: Int) {
        val adapter = binding.sPlataforma.adapter as ArrayAdapter<Plataforma>
        for (position in 0 until adapter.count) {
            if (adapter.getItem(position)?.ID_PLATAFORMA == id) {
                binding.sPlataforma.setSelection(position)
                break
            }
        }
    }
    fun createGame(){
        val fileName = UUID.randomUUID().toString()
        lifecycleScope.launch {
            val objeto = ProductModiCreate(
                TITULO=binding.edTituloCreacion.text.toString(),
                DESCRIPCION=binding.edDescriptionCreacion.text.toString(),
                PRECIO_FINAL=binding.edPrecioCreacOn.text.toString().toFloat(),
                ID_PLATAFORMA=seleccionadaPlataforma,
                ID_TIPO=seleccionadaTipo,
                IMAGEN=fileName
            )
            serviceRetrofit.createGame(objeto)
            runOnUiThread {
                uploadImageToFirebase("$fileName.jpg")
                Toast.makeText(context,"Se ha creado el producto",Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun modificarGame(){
        var fileName = UUID.randomUUID().toString()
        if(imageUri == null){
            fileName = Almacen.selectecGame.IMAGEN
        }

        lifecycleScope.launch {
            val objeto = ProductModiCreate(
                TITULO=binding.edTituloCreacion.text.toString(),
                DESCRIPCION=binding.edDescriptionCreacion.text.toString(),
                PRECIO_FINAL=binding.edPrecioCreacOn.text.toString().toFloat(),
                ID_PLATAFORMA=seleccionadaPlataforma,
                ID_TIPO=seleccionadaTipo,
                IMAGEN=fileName
            )
            serviceRetrofit.modifyGame(Almacen.selectecGame.ID_PRODUCTO,objeto)
            runOnUiThread {
                if(imageUri != null){
                    uploadImageToFirebase("$fileName.jpg")
                }else{
                    finish()
                }
                Toast.makeText(context,"Se ha modificado el producto",Toast.LENGTH_SHORT).show()
            }
        }
    }
}