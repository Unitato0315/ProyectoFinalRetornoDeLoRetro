package com.example.elretornodeloretro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterGameCardList
import com.example.elretornodeloretro.databinding.FragmentGamesListBinding
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GamesListFragment : Fragment() {
    lateinit var binding: FragmentGamesListBinding
    lateinit var recyclerGames: RecyclerView
    lateinit var myAdapter : AdapterGameCardList
    lateinit var service: ServiceRetrofit
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamesListBinding.inflate(inflater,container,false)
        service = RetrofitServiceFactory.makeRetrofitService(requireContext())

        recyclerGames = binding.rvGamesList
        recyclerGames.setHasFixedSize(true)
        recyclerGames.layoutManager = GridLayoutManager(requireContext(),1)

        myAdapter = AdapterGameCardList(Almacen.games.toMutableList(),requireContext())
        recyclerGames.adapter =  myAdapter

        binding.swrGamesList.setOnRefreshListener {
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
                activity?.runOnUiThread {
                    Almacen.games=listGames
                    updateGames(Almacen.games)
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(),"Se ha producido un error", Toast.LENGTH_LONG).show()
            }
        }
        binding.swrGamesList.isRefreshing=false
    }
    private fun updateGames(games: Array<Game>){
        myAdapter.listGames= games.toMutableList()
        myAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

}