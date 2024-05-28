package com.example.elretornodeloretro

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elretornodeloretro.databinding.ActivityGameDetailBinding

class GameDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityGameDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}