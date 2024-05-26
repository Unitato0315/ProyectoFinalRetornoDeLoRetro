package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.model.Game

class AdapterShopCart(var listProduct: MutableList<Game>, var context: Context):RecyclerView.Adapter<AdapterShopCart.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listProduct[position]
        holder.bind(item,context,position,this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vista: View? = null
        vista = LayoutInflater.from(parent.context).inflate(R.layout.shop_product,parent,false)
        val viewHolder = ViewHolder(vista!!)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        @SuppressLint("ResourceAsColor")
        fun bind(
            game: Game,
            context: Context,
            pos: Int,
            adapter: AdapterShopCart
        ) {
        }

    }
}