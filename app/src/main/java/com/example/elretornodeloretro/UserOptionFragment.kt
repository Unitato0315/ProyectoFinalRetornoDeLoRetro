package com.example.elretornodeloretro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.elretornodeloretro.databinding.FragmentUserOptionBinding
import com.example.elretornodeloretro.io.GeneralFuntion
import com.example.elretornodeloretro.io.TokenManage
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserOptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserOptionFragment : Fragment() {
    lateinit var binding: FragmentUserOptionBinding
    lateinit var tokenManage : TokenManage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserOptionBinding.inflate(layoutInflater, container,false)
        tokenManage = TokenManage(requireContext())

        val claim = GeneralFuntion.decodeJWT(tokenManage.getToken().toString())

        if(claim!!["ID_ROL"].toString().toInt() == 99){
            binding.btnModificar.visibility = View.GONE
            binding.btnModificar.isEnabled = false
        }else{
            //binding.btnModificar.visibility = View.VISIBLE
            binding.btnModificar.isEnabled = true
        }

        binding.btnCerrarSesion.setOnClickListener {
            tokenManage.deleteToken()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().finish()
            requireActivity().startActivity(intent)
        }

        binding.btnAcercaDe.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("ACERCA DE: ")
                .setMessage("Este es un proyecto creado por Jose Vicente Vargas Mestanza. Creado para el proyecto final de DAM 2023/24")
                .show()
        }

        binding.btnContactanos.setOnClickListener {
            val destinatario = "jvsonic9@gmail.com"
            val asunto = "Contacto con la tienda"


            
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(destinatario))
                putExtra(Intent.EXTRA_SUBJECT, asunto)
                putExtra(Intent.EXTRA_TEXT, "")
            }

            startActivity(intent)
        }

        binding.btnPedidos.setOnClickListener {
            val inte = Intent(requireActivity(),PedidosActivity::class.java)
            requireActivity().startActivity(inte)
        }

        binding.btnModificar.setOnClickListener {
            if(claim!!["ID_ROL"].toString().toInt() != 99){
                val inte = Intent(requireActivity(), ModificarDatosFacturacion::class.java)
                requireActivity().startActivity(inte)
            }else{
                Toast.makeText(context,"Un usuario administrador no necesita datos de facturacion", Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }


}