package com.example.elretornodeloretro.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.elretornodeloretro.Fragment_games
import com.example.elretornodeloretro.LoginFragment
import com.example.elretornodeloretro.MainActivity
import com.example.elretornodeloretro.shoppingFragment
import com.example.elretornodeloretro.userOptionFragment
import com.example.elretornodeloretro.whiteListFragment

class AdapterViewPageNormalUser (fragment: MainActivity): FragmentStateAdapter(fragment) {
    private val fragmentList = listOf(
        Fragment_games(),
        shoppingFragment(),
        whiteListFragment(),
        userOptionFragment()
        // Agrega aquí otros fragmentos si es necesario
    )
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                Fragment_games()
            }
            1->{
                shoppingFragment()
            }
            2->{
                whiteListFragment()
            }
            3->{
                userOptionFragment()
            }
            else->{throw Resources.NotFoundException("Posicion no encontrada")
            }
        }
        return fragmentList[position]
    }
}