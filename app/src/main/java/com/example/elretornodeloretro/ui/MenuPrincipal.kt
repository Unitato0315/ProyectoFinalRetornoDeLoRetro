package com.example.elretornodeloretro.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elretornodeloretro.databinding.ActivityMenuPrincipalBinding
import com.example.elretornodeloretro.util.PreferenceHelper
import com.example.elretornodeloretro.util.PreferenceHelper.set

class MenuPrincipal : AppCompatActivity() {
    private lateinit var binding: ActivityMenuPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEliminarToken.setOnClickListener{

        }
    }
}