package com.example.elretornodeloretro

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterGameCard
import com.example.elretornodeloretro.adapter.AdapterViewPage
import com.example.elretornodeloretro.databinding.ActivityMainBinding
import com.example.elretornodeloretro.databinding.FragmentGamesBinding
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Fragment_games : Fragment() {
    lateinit var binding: FragmentGamesBinding
    lateinit var recyclerGames: RecyclerView
    lateinit var myAdapter : AdapterGameCard
    lateinit var service: ServiceRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGamesBinding.inflate(inflater,container,false)
        service = RetrofitServiceFactory.makeRetrofitService(requireContext())

        recyclerGames = binding.rvGames
        recyclerGames.setHasFixedSize(true)
        recyclerGames.layoutManager = GridLayoutManager(requireContext(),2)


        myAdapter = AdapterGameCard(Almacen.games.toMutableList(),requireContext())
        recyclerGames.adapter =  myAdapter

        binding.swrGames.setOnRefreshListener {
            refreshData()
        }

        return binding.root
    }

    private fun refreshData(){
        lifecycleScope.launch {
            try{
                val listGames = withContext(Dispatchers.IO){
                    service.listGames()
                }
                Almacen.games=listGames
                updateGames(Almacen.games)
            }catch (e: Exception){
                Toast.makeText(requireContext(),"Se ha producido un error",Toast.LENGTH_LONG).show()
            }
        }
        binding.swrGames.isRefreshing=false
    }
    private fun updateGames(games: Array<Game>){
        myAdapter.listGames= games.toMutableList()
        myAdapter.notifyDataSetChanged()
    }
}