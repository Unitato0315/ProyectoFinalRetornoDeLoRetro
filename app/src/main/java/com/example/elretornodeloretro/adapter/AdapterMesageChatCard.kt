package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.GameDetailActivity
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.MensajeChat
import com.example.elretornodeloretro.model.Message
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class AdapterMesageChatCard(var listMensajes: MutableList<MensajeChat>, var context: Context): RecyclerView.Adapter<AdapterMesageChatCard.ViewHolder>() {

    override fun onBindViewHolder(holder: AdapterMesageChatCard.ViewHolder, position: Int) {
        val item = listMensajes[position]
        holder.bind(item,context,position,this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var vista: View? = null
        vista = LayoutInflater.from(parent.context).inflate(R.layout.mensaje_card,parent,false)
        val viewHolder = ViewHolder(vista!!)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return  listMensajes.size
    }
    class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        val tvOtro : TextView = view.findViewById(R.id.tvMensajeOtro)
        val tvPropio: TextView = view.findViewById(R.id.tvMensajePropio)
        lateinit var tokenManage : TokenManage
        @SuppressLint("ResourceAsColor")
        fun bind(
            mensaje: MensajeChat,
            context: Context,
            pos: Int,
            adapter: AdapterMesageChatCard
        ){
            tokenManage = TokenManage(context)
            val claim = GeneralFuntion.decodeJWT(tokenManage.getToken()!!)
            //compruebo el rol para saber donde colocar cada mensaje
            if(claim!!["ID_ROL"].toString().toInt() == 99){
                if(mensaje.ADMIN == 1){
                    tvPropio.text = mensaje.MENSAJE
                    tvOtro.visibility = View.INVISIBLE
                }else{
                    tvOtro.text = mensaje.MENSAJE
                    tvPropio.visibility = View.INVISIBLE
                }
            }else{
                if(mensaje.ADMIN == 0){
                    tvPropio.text = mensaje.MENSAJE
                    tvOtro.visibility = View.INVISIBLE
                }else{
                    tvOtro.text = mensaje.MENSAJE
                    tvPropio.visibility = View.INVISIBLE
                }
            }
        }
    }
}