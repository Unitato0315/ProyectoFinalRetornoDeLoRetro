package com.example.elretornodeloretro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.elretornodeloretro.databinding.FragmentLoginBinding
import com.example.elretornodeloretro.io.TokenManage
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.PostModelLogin
import com.example.elretornodeloretro.model.UserLogin
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var service: ServiceRetrofit
    lateinit var tokenManage: TokenManage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container,false)
        service = RetrofitServiceFactory.makeRetrofitService(requireContext())
        tokenManage = TokenManage(requireContext())

        binding.btnLogin.setOnClickListener {
            //val intent = Intent(requireActivity(), MainActivity::class.java)
            //requireActivity().finish()
            //requireActivity().startActivity(intent)
            login()
        }

        return binding.root
    }

    fun login(){
        lifecycleScope.launch {
            val objRequest = PostModelLogin(
                username = binding.edtUsuario.text.toString(),
                password = binding.edtPassword.text.toString())

            try {
                val response = service.signIn(objRequest)
                handleLoginSuccess(response)
            }catch (e: HttpException){
                if(e.code()==400){
                    handleLoginError()
                }else{
                    Toast.makeText(context,"Se ha producido un error", Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Toast.makeText(context,"Fallo en la solicitud", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun handleLoginSuccess(successResponse: UserLogin?) {
        if (successResponse != null) {
            Toast.makeText(requireContext(),"Se ha iniciado sesion correctamente",Toast.LENGTH_SHORT).show()
            tokenManage.saveToken(successResponse.token)
        }
    }

    private fun handleLoginError() {

        Toast.makeText(requireContext(),"Se han introducido mal los datos",Toast.LENGTH_SHORT).show()

    }
}