package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.model.Game
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class AdapterGameCard(var listGames: Array<Game>, var context: Context): RecyclerView.Adapter<AdapterGameCard.ViewHolder>() {

    lateinit var tokenManage: TokenManage
    lateinit var idRol: String
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listGames[position]
        holder.bind(item,context,position,this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vista: View? = null
        tokenManage = TokenManage(context)
        val token = tokenManage.getToken()

        if(token.isNullOrBlank()){
           vista = LayoutInflater.from(parent.context).inflate(R.layout.game_card,parent,false)
        }else{
            val claims = GeneralFuntion.decodeJWT(token)
            if (claims != null){
                idRol = claims["ID_ROL"]?.toString()!!
                vista = if(idRol =="99"){
                    LayoutInflater.from(parent.context).inflate(R.layout.game_card2,parent,false)
                }else{
                    LayoutInflater.from(parent.context).inflate(R.layout.game_card3,parent,false)
                }
            }else{
                vista = LayoutInflater.from(parent.context).inflate(R.layout.game_card,parent,false)
            }
        }
        val viewHolder =ViewHolder(vista!!)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listGames.size
    }
    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imGame)
        val title: TextView = view.findViewById(R.id.labelTitulo)
        val view: View = view
        lateinit var tokenManage: TokenManage
        lateinit var idRol: String
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

            tokenManage = TokenManage(context)
            val token = tokenManage.getToken()

            if(token.isNullOrBlank()){
                val precio: TextView = view.findViewById(R.id.labelPrecio)
                precio.text = game.PRECIO_FINAL.toString()
            }else{
                val claims = GeneralFuntion.decodeJWT(token)
                if (claims != null){
                    idRol = claims["ID_ROL"]?.toString()!!
                    if(idRol =="99"){

                    }else{

                    }
                }else{

                }
            }

            title.text = game.TITULO
        }

    }

}