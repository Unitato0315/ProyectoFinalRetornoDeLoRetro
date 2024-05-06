package com.example.elretornodeloretro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.elretornodeloretro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG = "JVVM"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}