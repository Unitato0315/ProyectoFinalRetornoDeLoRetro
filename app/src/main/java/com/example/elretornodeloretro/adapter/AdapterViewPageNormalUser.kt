package com.example.elretornodeloretro.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.elretornodeloretro.Fragment_games
import com.example.elretornodeloretro.MainActivity
import com.example.elretornodeloretro.ShoppingFragment
import com.example.elretornodeloretro.UserOptionFragment
import com.example.elretornodeloretro.WhiteListFragment

class AdapterViewPageNormalUser (fragment: MainActivity): FragmentStateAdapter(fragment) {
    private val fragmentList = listOf(
        Fragment_games(),
        ShoppingFragment(),
        WhiteListFragment(),
        UserOptionFragment()
        // Agrega aquí otros fragmentos si es necesario
    )
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                Fragment_games()
            }
            1->{
                ShoppingFragment()
            }
            2->{
                WhiteListFragment()
            }
            3->{
                UserOptionFragment()
            }
            else->{throw Resources.NotFoundException("Posicion no encontrada")
            }
        }
        return fragmentList[position]
    }
}