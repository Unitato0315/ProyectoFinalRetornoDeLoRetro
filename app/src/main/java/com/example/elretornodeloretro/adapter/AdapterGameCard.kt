package com.example.elretornodeloretro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.model.Game

class AdapterGameCard(var listGames: ArrayList<Game>,var context: Context): RecyclerView.Adapter<AdapterGameCard.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.game_card,parent,false)
        val viewHolder =ViewHolder(vista)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listGames.size
    }
    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        //val image = view.findViewById()
    }

}