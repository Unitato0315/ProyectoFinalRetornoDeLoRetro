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

class AdapterMesageChatCardAdmin(var listMensajes: MutableList<MensajeChat>, var context: Context): RecyclerView.Adapter<AdapterMesageChatCardAdmin.ViewHolder>() {

    override fun onBindViewHolder(holder: AdapterMesageChatCardAdmin.ViewHolder, position: Int) {
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
            adapter: AdapterMesageChatCardAdmin
        ){
            tokenManage = TokenManage(context)
            if(mensaje.ADMIN == 1){
                tvPropio.text = mensaje.MENSAJE
                tvPropio.visibility = View.VISIBLE
                tvOtro.visibility = View.GONE
            }else{
                tvOtro.text = mensaje.MENSAJE
                tvOtro.visibility = View.VISIBLE
                tvPropio.visibility = View.GONE
            }
        }
    }
}