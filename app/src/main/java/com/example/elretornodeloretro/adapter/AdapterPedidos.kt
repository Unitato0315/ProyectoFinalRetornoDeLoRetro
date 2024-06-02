package com.example.elretornodeloretro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.GameDetailActivity
import com.example.elretornodeloretro.R
import com.example.elretornodeloretro.ResumenPedidoActivity
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.Game
import com.example.elretornodeloretro.model.Order

class AdapterPedidos(var listPedidos: MutableList<Order>, var context: Context):RecyclerView.Adapter<AdapterPedidos.ViewHolder>() {

    override fun onBindViewHolder(holder: AdapterPedidos.ViewHolder, position: Int) {
        val item = listPedidos[position]
        holder.bind(item,context,position,this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPedidos.ViewHolder {
        var vista: View? = null
        vista = LayoutInflater.from(parent.context).inflate(R.layout.pedidos_card,parent,false)
        val viewHolder = AdapterPedidos.ViewHolder(vista!!)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return listPedidos.size
    }

    class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        val precio: TextView = view.findViewById(R.id.tvTotalPedido)
        val nPedido: TextView = view.findViewById(R.id.tvNPedido)
        val estado_nombre: TextView = view.findViewById(R.id.tvUsuario)
        val linearLayout: LinearLayout = view.findViewById(R.id.lnPedidoLista)
        lateinit var tokenManage : TokenManage
        @SuppressLint("ResourceAsColor")
        fun bind(
            pedido: Order,
            context: Context,
            pos: Int,
            adapter: AdapterPedidos
        ){
            tokenManage = TokenManage(context)

            val claim = GeneralFuntion.decodeJWT(tokenManage.getToken()!!)

            nPedido.text = "Nº "+pedido.ID_PEDIDO.toString()
            precio.text = "${String.format("%.2f",pedido.TOTAL)}€"

            if(claim!!["ID_ROL"].toString().toInt() == 99){
                estado_nombre.text = pedido.NOMBRE_ESTADO+"\n"+pedido.NOMBRE+" "+pedido.APELLIDO
            }else{
                estado_nombre.text = pedido.NOMBRE_ESTADO
            }

            linearLayout.setOnClickListener {
                Almacen.selectecPedido = pedido


                var inte: Intent = Intent(context, ResumenPedidoActivity::class.java)
                ContextCompat.startActivity(context, inte, null)
            }
        }
    }

}