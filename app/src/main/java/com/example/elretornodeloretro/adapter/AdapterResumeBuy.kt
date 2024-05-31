package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.model.Game

class AdapterResumeBuy(var listProduct: MutableList<Game>, var context: Context): RecyclerView.Adapter<AdapterResumeBuy.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listProduct[position]
        holder.bind(item,context,position,this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vista: View? = null
        vista = LayoutInflater.from(parent.context).inflate(R.layout.resume_product,parent,false)
        val viewHolder = ViewHolder(vista!!)


        return viewHolder
    }
    override fun getItemCount(): Int {
        return listProduct.size
    }
    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvTitleResume)
        val precio: TextView = view.findViewById(R.id.tvPriceResume)
        @SuppressLint("ResourceAsColor")
        fun bind(
            game: Game,
            context: Context,
            pos: Int,
            adapter: AdapterResumeBuy
        ){
            title.text = game.TITULO
            precio.text = "${String.format("%.2f",game.PRECIO_FINAL)}€"
        }
    }
}