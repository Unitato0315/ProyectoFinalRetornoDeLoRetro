package com.example.elretornodeloretro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterGameCard
import com.example.elretornodeloretro.databinding.FragmentGamesBinding
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game

class Fragment_games : Fragment() {
    lateinit var binding: FragmentGamesBinding
    lateinit var recyclerGames: RecyclerView
    lateinit var myAdapter : AdapterGameCard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGamesBinding.inflate(inflater,container,false)

        recyclerGames = binding.rvGames
        recyclerGames.setHasFixedSize(true)
        recyclerGames.layoutManager = GridLayoutManager(requireContext(),2)

        myAdapter = AdapterGameCard(Almacen.games,requireContext())

        recyclerGames.adapter =  myAdapter

        return binding.root

    }

}