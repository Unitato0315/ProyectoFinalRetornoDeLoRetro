package com.example.elretornodeloretro.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.elretornodeloretro.MainActivity
import com.example.elretornodeloretro.FactureFragment
import com.example.elretornodeloretro.GamesListFragment
import com.example.elretornodeloretro.UserOptionFragment

class AdapterViewPageAdmin(fragment: MainActivity): FragmentStateAdapter(fragment) {
    private val fragmentList = listOf(
        GamesListFragment(),
        FactureFragment(),
        UserOptionFragment()
        // Agrega aquí otros fragmentos si es necesario
    )
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                GamesListFragment()
            }
            1->{
                FactureFragment()
            }
            2->{
                UserOptionFragment()
            }
            else->{throw Resources.NotFoundException("Posicion no encontrada")
            }
        }
        return fragmentList[position]
    }
}