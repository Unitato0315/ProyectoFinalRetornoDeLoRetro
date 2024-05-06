package com.example.elretornodeloretro.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.elretornodeloretro.databinding.ActivityMainBinding
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class MainActivity : AppCompatActivity() {
    val TAG = "JVVM"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    @Composable
    @Destination(start = true)
    fun AuthScreen(
        navigator: DestinationsNavigator,
        viewModel: MainViewModel = hiltViewModel()
    ) {
        val state = viewModel.state
        val context = LocalContext.current
        LaunchedEffect(viewModel,context) {
            
        }
    }
}