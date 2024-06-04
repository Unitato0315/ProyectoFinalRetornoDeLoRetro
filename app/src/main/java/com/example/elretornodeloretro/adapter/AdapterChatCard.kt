package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.ChatActivity
import com.example.elretornodeloretro.GameDetailActivity
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.DatosChat
import com.example.elretornodeloretro.model.Game
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class AdapterChatCard(var listChat: MutableList<DatosChat>,var context: Context): RecyclerView.Adapter<AdapterChatCard.ViewHolder>() {
    override fun onBindViewHolder(holder: AdapterChatCard.ViewHolder, position: Int) {
        val item = listChat[position]
        holder.bind(item,context,position,this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterChatCard.ViewHolder {
        var vista: View? = null
        vista = LayoutInflater.from(parent.context).inflate(R.layout.chat_cart,parent,false)
        val viewHolder = AdapterChatCard.ViewHolder(vista!!)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listChat.size
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private lateinit var service: ServiceRetrofit
        private lateinit var tokenManage: TokenManage
        val motivo: TextView = view.findViewById(R.id.tvMotivo)
        val tituloMotivo: TextView = view.findViewById(R.id.tvTituloMotivo)
        val imWarning: ImageView = view.findViewById(R.id.imAdvertencia)
        val lnChat: LinearLayout = view.findViewById(R.id.lnChat)

        @SuppressLint("ResourceAsColor")
        fun bind(
            chat: DatosChat,
            context: Context,
            pos: Int,
            adapter: AdapterChatCard
        ){
            service = RetrofitServiceFactory.makeRetrofitService(context)
            tokenManage = TokenManage(context)
            val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
            val idRol = claim!!["ID_ROL"].toString().toInt()

            motivo.text = chat.NOMBRE_TIPO_CHAT
            //Comprobacion del rol para saber quien a sido el ultimo en escribir
            if(idRol == 99){
                tituloMotivo.text = "MOTIVO: "+chat.TITULO+"\nUSUARIO: "+chat.NOMBRE+" "+chat.APELLIDO
                if(chat.ULTIMO_MENSAJE == 0){
                    imWarning.visibility = View.VISIBLE
                }else{
                    imWarning.visibility = View.INVISIBLE
                }
            }else{
                tituloMotivo.text ="MOTIVO: "+ chat.TITULO
                if(chat.ULTIMO_MENSAJE == 1){
                    imWarning.visibility = View.VISIBLE
                }else{
                    imWarning.visibility = View.INVISIBLE
                }
            }

            lnChat.setOnClickListener {
                Almacen.selectecChat = chat
                var inte: Intent = Intent(context, ChatActivity::class.java)
                ContextCompat.startActivity(context, inte, null)
            }
        }
    }
}