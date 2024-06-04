package com.example.elretornodeloretro.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.elretornodeloretro.ChatFragment
import com.example.elretornodeloretro.MainActivity
import com.example.elretornodeloretro.GamesListFragment
import com.example.elretornodeloretro.UserOptionFragment

class AdapterViewPageAdmin(fragment: MainActivity): FragmentStateAdapter(fragment) {
    private val fragmentList = listOf(
        GamesListFragment(),
        ChatFragment(),
        UserOptionFragment()
    )
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                GamesListFragment()
            }
            1->{
                ChatFragment()
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