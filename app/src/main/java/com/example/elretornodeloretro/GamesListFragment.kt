package com.example.elretornodeloretro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        binding.svGamesList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // No hacer nada aquí
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapter.filter.filter(newText)
                return true
            }
        })

        binding.btnCrearProducto.setOnClickListener {
            var inte: Intent = Intent(requireContext(), CrearModificarProducto::class.java).apply {
                putExtra("modificar",false)
            }
            ContextCompat.startActivity(requireContext(), inte, null)
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