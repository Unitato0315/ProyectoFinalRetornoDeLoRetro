package com.example.elretornodeloretro

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elretornodeloretro.databinding.ActivityGameDetailBinding
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class GameDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameDetailBinding
    lateinit var tokenManage: TokenManage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbGame)
        binding.tbGame.title = Almacen.selectecGame.TITULO
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tokenManage = TokenManage(this)

        if(tokenManage.getToken().isNullOrBlank()){
            binding.btnAddCart.isEnabled = false
            binding.btnAddCart.visibility = View.INVISIBLE
            binding.btnAddCart.text = "Inicia sesion para comprar"
        }

        binding.tbGame.setNavigationOnClickListener {
            finish()
        }

        var storage = Firebase.storage

        var storageRef = storage.reference
        var spaceRef = storageRef.child("productos/${Almacen.selectecGame.IMAGEN}.jpg")
        val localfile  = File.createTempFile("tempImage","jpg")
        spaceRef.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            val scaleBitmap = Bitmap.createScaledBitmap(bitmap,400,400,true)
            binding.imGameDetail.setImageBitmap(scaleBitmap)
        }.addOnFailureListener{
            Toast.makeText(this,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
        }

        binding.edDescription.setText(Almacen.selectecGame.DESCRIPCION)
        binding.edTipo.setText(Almacen.selectecGame.NOMBRE_TIPO)
        binding.edPlataforma.setText(Almacen.selectecGame.NOMBRE_PLATAFORMA)
        binding.tvPrecioDetail.text = "Total: ${String.format("%.2f",Almacen.selectecGame.PRECIO_FINAL)}€"

        binding.btnAddCart.setOnClickListener {
            if(!Almacen.cart.contains(Almacen.selectecGame)){
                Almacen.totalPrice += Almacen.selectecGame.PRECIO_FINAL
                Almacen.cart.add(Almacen.selectecGame)
                finish()
            }else{
                Toast.makeText(this,"Ya tienes este producto en el carrito",Toast.LENGTH_SHORT).show()
            }
        }
    }
}