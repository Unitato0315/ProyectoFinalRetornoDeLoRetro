package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.model.Game
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.platform.Platform
import java.io.File

class AdapterGameCard(var listGames: MutableList<Game>, var context: Context): RecyclerView.Adapter<AdapterGameCard.ViewHolder>() {

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

            var titleText: String
            if(game.TITULO.length > 40 ){
                titleText = game.TITULO.substring(39)+"..."
            }else{
                titleText = game.TITULO
            }
            title.text = titleText
            precio.text = "${String.format("%.2f",game.PRECIO_FINAL)}€"
            platform.text = game.NOMBRE_PLATAFORMA.toString()



        }
    }

}