package com.example.elretornodeloretro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterGameCard
import com.example.elretornodeloretro.databinding.FragmentGamesBinding
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Fragment_games : Fragment() {
    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerGames: RecyclerView
    private lateinit var myAdapter: AdapterGameCard
    private lateinit var service: ServiceRetrofit
    private lateinit var tokenManage: TokenManage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service = RetrofitServiceFactory.makeRetrofitService(requireContext())
        tokenManage = TokenManage(requireContext())

        recyclerGames = binding.rvGames
        recyclerGames.setHasFixedSize(true)
        recyclerGames.layoutManager = GridLayoutManager(requireContext(), 2)

        myAdapter = AdapterGameCard(Almacen.games.toMutableList(), requireContext())
        recyclerGames.adapter = myAdapter

        binding.swrGames.setOnRefreshListener {
            refreshData()
        }

        refreshData() // Inicializa los datos al crear la vista
    }

    private fun refreshData() {
        lifecycleScope.launch {
            try {
                val listGames = withContext(Dispatchers.IO) {
                    service.listGames()
                }
                if (isAdded) {  // Verifica si el fragmento está adjunto antes de usar el contexto
                    activity?.runOnUiThread {
                        Almacen.games = listGames
                        updateGames(Almacen.games)
                    }
                }
            } catch (e: Exception) {
                if (isAdded) {  // Verifica si el fragmento está adjunto antes de usar el contexto
                    Toast.makeText(requireContext(), "Se ha producido un error", Toast.LENGTH_LONG).show()
                }
            }
            binding.swrGames.isRefreshing = false
        }
    }

    private fun updateGames(games: Array<Game>) {
        myAdapter.listGames = games.toMutableList()
        myAdapter.notifyDataSetChanged()
    }


}