package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.GameDetailActivity
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class AdapterGameCard(var listGames: MutableList<Game>, var context: Context): RecyclerView.Adapter<AdapterGameCard.ViewHolder>(),Filterable {

    private var listGamesFull: MutableList<Game> = listGames.toMutableList()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listGames[position]
        holder.bind(item,context,position,this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vista: View? = null
        vista = LayoutInflater.from(parent.context).inflate(R.layout.game_card,parent,false)
        val viewHolder =ViewHolder(vista!!)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listGames.size
    }
    fun removeItem(position: Int) {
        listGames.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listGames.size)
    }
    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imGame)
        val title: TextView = view.findViewById(R.id.labelTitulo)
        val precio: TextView = view.findViewById(R.id.labelPrecio)
        val platform: TextView = view.findViewById(R.id.labelPlataforma)
        val view: View = view
        val linearLayout: LinearLayout = view.findViewById(R.id.liLaGame)
        @SuppressLint("ResourceAsColor")
        fun bind(
            game: Game,
            context: Context,
            pos: Int,
            adapter: AdapterGameCard
        ){
            var storage = Firebase.storage

            var storageRef = storage.reference
            var spaceRef = storageRef.child("productos/${game.IMAGEN}.jpg")
            val localfile  = File.createTempFile("tempImage","jpg")
            spaceRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                val scaleBitmap = Bitmap.createScaledBitmap(bitmap,200,200,true)
                image.setImageBitmap(scaleBitmap)
            }.addOnFailureListener{
                Toast.makeText(context,"Algo ha fallado en la descarga", Toast.LENGTH_SHORT).show()
            }
            var titleText: String = if(game.TITULO.length > 40 ){
                game.TITULO.substring(39)+"..."
            }else{
                game.TITULO
            }
            title.text = titleText
            precio.text = "${String.format("%.2f",game.PRECIO_FINAL)}€"
            platform.text = game.NOMBRE_PLATAFORMA.toString()

            linearLayout.setOnClickListener {
                Almacen.selectecGame = game
                var inte: Intent = Intent(context, GameDetailActivity::class.java)
                ContextCompat.startActivity(context, inte, null)
            }

        }
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterdList = mutableListOf<Game>()
                if(constraint.isNullOrEmpty()){
                    filterdList.addAll(listGamesFull)
                }else{
                    val filterPatterm = constraint.toString().toLowerCase().trim()
                    for (item in listGames){
                        if(item.TITULO.toLowerCase().contains(filterPatterm)){
                            filterdList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filterdList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listGames.clear()
                listGames.addAll(results?.values as List<Game>)
                notifyDataSetChanged()
            }

        }
    }

}