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
import com.example.elretornodeloretro.model.ProductOrder

class AdapterResumePedido(var listProduct: MutableList<ProductOrder>, var context: Context): RecyclerView.Adapter<AdapterResumePedido.ViewHolder>() {


    override fun onBindViewHolder(holder: AdapterResumePedido.ViewHolder, position: Int) {
        val item = listProduct[position]
        holder.bind(item,context,position,this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterResumePedido.ViewHolder {
        var vista: View? = null
        vista = LayoutInflater.from(parent.context).inflate(R.layout.resume_product,parent,false)
        val viewHolder = AdapterResumePedido.ViewHolder(vista!!)


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
            game: ProductOrder,
            context: Context,
            pos: Int,
            adapter: AdapterResumePedido
        ){
            title.text = game.TITULO
            precio.text = "${String.format("%.2f",game.PRECIO_FINAL)}€"
        }
    }
}