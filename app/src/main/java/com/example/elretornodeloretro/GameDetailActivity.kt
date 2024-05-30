package com.example.elretornodeloretro

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elretornodeloretro.databinding.ActivityGameDetailBinding
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game

class GameDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tbGame)
        binding.tbGame.title = Almacen.selectecGame.TITULO
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tbGame.setNavigationOnClickListener {
            finish()
        }

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