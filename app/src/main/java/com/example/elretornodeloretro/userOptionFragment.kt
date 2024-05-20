package com.example.elretornodeloretro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.elretornodeloretro.databinding.FragmentUserOptionBinding
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.model.Almacen

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [userOptionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class userOptionFragment : Fragment() {
    lateinit var binding: FragmentUserOptionBinding
    lateinit var tokenManage : TokenManage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserOptionBinding.inflate(layoutInflater, container,false)
        tokenManage = TokenManage(requireContext())
        binding.btnBorrarToken.setOnClickListener {
            tokenManage.deleteToken()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().finish()
            requireActivity().startActivity(intent)
        }

        return binding.root
    }


}