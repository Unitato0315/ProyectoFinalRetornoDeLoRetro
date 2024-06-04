package com.example.elretornodeloretro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterChatCard
import com.example.elretornodeloretro.databinding.FragmentChatBinding
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen
import com.example.elretornodeloretro.model.CrearChat
import com.example.elretornodeloretro.model.DatosChat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatFragment : Fragment() {
    lateinit var binding: FragmentChatBinding
    private lateinit var recyclerChat: RecyclerView
    private lateinit var adapterChat: AdapterChatCard
    private lateinit var service: ServiceRetrofit
    private lateinit var tokenManage: TokenManage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        service = RetrofitServiceFactory.makeRetrofitService(requireContext())
        tokenManage = TokenManage(requireContext())

        service = RetrofitServiceFactory.makeRetrofitService(requireContext())
        tokenManage = TokenManage(requireContext())
        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
        val idRol = claim!!["ID_ROL"].toString().toInt()

        if(idRol == 99){
            binding.imNuevoChat.visibility = View.INVISIBLE
        }

        binding.swrChat.setOnRefreshListener {
            refreshData()
        }

        recyclerChat = binding.rvChat
        recyclerChat.setHasFixedSize(true)
        recyclerChat.layoutManager = GridLayoutManager(requireContext(),1)
        adapterChat = AdapterChatCard(Almacen.listaChat,requireContext())
        recyclerChat.adapter = adapterChat

        val motivosChat = arrayOf("Selecciona un motivo","Consulta","Otros")
        var selectecMotivo = 0
        val adapterMotivo = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,motivosChat)

        binding.imNuevoChat.setOnClickListener {
            var builder = MaterialAlertDialogBuilder(requireContext())
            val inflater = layoutInflater
            builder.setTitle("")
            val dialogLayout = inflater.inflate(R.layout.dialog_crear_chat,null)
            val spinner :Spinner =dialogLayout.findViewById(R.id.sMotivos)
            val motivo: EditText = dialogLayout.findViewById(R.id.edExplicacion)
            val crear: Button = dialogLayout.findViewById(R.id.btnCrearChat)
            spinner.adapter = adapterMotivo
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectecMotivo = position
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
            builder.setView(dialogLayout)
            val alertDialog = builder.create()
            crear.setOnClickListener {
                if(!motivo.text.toString().isNullOrBlank() && selectecMotivo != 0){
                    crearChat(selectecMotivo,motivo.text.toString())
                    alertDialog.dismiss()
                }else{
                    Toast.makeText(requireContext(),"No se han introducido todos los datos",Toast.LENGTH_SHORT).show()
                }
            }

            alertDialog.show()

        }

        refreshData()
    }

    private fun refreshData() {
        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
        val idRol = claim!!["ID_ROL"].toString().toInt()
        lifecycleScope.launch {
            try {
                val listChat = withContext(Dispatchers.IO) {
                    if(idRol == 99){
                        service.getAllChat()
                    }else{
                        service.getChat(claim!!["ID_USUARIO"].toString().toInt())
                    }
                }
                if (isAdded) {  // Verifica si el fragmento está adjunto antes de usar el contexto
                    activity?.runOnUiThread {
                        Almacen.listaChat = listChat.toMutableList()
                        updateChat(Almacen.listaChat)
                    }
                }
            } catch (e: Exception) {
                if (isAdded) {  // Verifica si el fragmento está adjunto antes de usar el contexto
                    Toast.makeText(requireContext(), "Se ha producido un error", Toast.LENGTH_LONG).show()
                }
            }
            binding.swrChat.isRefreshing = false
        }
    }

    private fun updateChat(chat: MutableList<DatosChat>) {
        adapterChat.listChat = chat.toMutableList()
        adapterChat.notifyDataSetChanged()
    }

    fun crearChat(motivo: Int,explicacion:String){
        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())
        val idUsuario = claim!!["ID_USUARIO"].toString().toInt()
        val objecto = CrearChat(
            TITULO = explicacion,
            ID_USUARIO = idUsuario,
            ID_TIPO_CHAT = motivo
        )
        lifecycleScope.launch {

            service.crearChat(objecto)
            activity?.runOnUiThread {
                refreshData()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
}