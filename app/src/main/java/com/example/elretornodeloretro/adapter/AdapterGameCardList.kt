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
import android.widget.LinearLayout
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
import java.io.File

class AdapterGameCardList(var listGames: MutableList<Game>, var context: Context): RecyclerView.Adapter<AdapterGameCardList.ViewHolder>() {

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

        vista = LayoutInflater.from(parent.context).inflate(R.layout.game_card_list_admin,parent,false)

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
        val view: View = view
        lateinit var tokenManage: TokenManage
        lateinit var idRol: String
        @SuppressLint("ResourceAsColor")
        fun bind(
            game: Game,
            context: Context,
            pos: Int,
            adapter: AdapterGameCardList
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
            //val token = tokenManage.getToken()
            title.text = game.TITULO


            val delete : ImageButton = view.findViewById(R.id.btnDelete)
            val modify: LinearLayout = view.findViewById(R.id.lyModify)

            delete.setOnClickListener {
                showDialog(context,game.TITULO,game.ID_PRODUCTO,adapter, pos,spaceRef)
            }

            modify.setOnClickListener {
                Toast.makeText(context,"Modificar", Toast.LENGTH_SHORT).show()
            }

        }
        fun showDialog(context: Context, title: String, idGame: Int, adapter: AdapterGameCardList, pos: Int, spaceReference: StorageReference){
            MaterialAlertDialogBuilder(context)
                .setTitle("Vas a eliminar el producto: $title")
                .setMessage("¿Estas seguro que quieres eliminarlo?")
                .setPositiveButton("Sí"){dialog, which ->
                    deleteGame(idGame,context)
                    adapter.removeItem(pos)
                    spaceReference.delete()
                }.setNegativeButton("No"){dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
        fun deleteGame(idGame: Int, context: Context){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitServiceFactory.makeRetrofitService(context).deleteGame(idGame)
                    if (response.isSuccessful)  {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "Se ha borrado correctamente", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        withContext(Dispatchers.Main){
                            Toast.makeText(context, "No tienes acceso a esta seccion", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {

                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Se ha producido un error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

}