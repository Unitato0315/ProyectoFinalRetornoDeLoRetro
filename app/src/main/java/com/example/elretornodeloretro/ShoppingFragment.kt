package com.example.elretornodeloretro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elretornodeloretro.adapter.AdapterShopCart
import com.example.elretornodeloretro.databinding.FragmentShoppingBinding
import com.example.elretornodeloretro.io.data.RetrofitServiceFactory
import com.example.elretornodeloretro.io.data.ServiceRetrofit
import com.example.elretornodeloretro.model.Almacen

class ShoppingFragment : Fragment() {

    lateinit var binding: FragmentShoppingBinding
    lateinit var adapter: AdapterShopCart
    lateinit var recyclerShop: RecyclerView
    lateinit var serviceRetrofit: ServiceRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingBinding.inflate(inflater,container,false)
        serviceRetrofit = RetrofitServiceFactory.makeRetrofitService(requireContext())
        binding.tvTotal.text = "${resources.getText(R.string.total)} ${String.format("%.2f",Almacen.totalPrice)}€"
        binding.btnPagar.text = "${resources.getText(R.string.pagar)} (${Almacen.cart.size})"

        recyclerShop = binding.rvShop
        recyclerShop.setHasFixedSize(true)
        recyclerShop.layoutManager = GridLayoutManager(requireContext(),1)
        adapter = AdapterShopCart(Almacen.cart,requireContext()) { updateData() }
        recyclerShop.adapter = adapter

        binding.btnPagar.setOnClickListener {

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tvTotal.text = "${resources.getText(R.string.total)} ${String.format("%.2f",Almacen.totalPrice)}€"
        binding.btnPagar.text = "${resources.getText(R.string.pagar)} (${Almacen.cart.size})"

        adapter.listProduct = Almacen.cart
        adapter.notifyDataSetChanged()
    }

    fun updateData(){
        binding.tvTotal.text = "${resources.getText(R.string.total)} ${String.format("%.2f",Almacen.totalPrice)}€"
        binding.btnPagar.text = "${resources.getText(R.string.pagar)} (${Almacen.cart.size})"
    }
}