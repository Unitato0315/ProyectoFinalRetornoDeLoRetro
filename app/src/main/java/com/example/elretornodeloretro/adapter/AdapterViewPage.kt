package com.example.elretornodeloretro.adapter

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.elretornodeloretro.Fragment_games
import com.example.elretornodeloretro.LoginFragment
import com.example.elretornodeloretro.MainActivity

class AdapterViewPage(fragment: MainActivity): FragmentStateAdapter(fragment) {
    private val fragmentList = listOf(
        Fragment_games(),
        LoginFragment()
    )
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{Fragment_games()}
            1->{LoginFragment()}
            else->{throw Resources.NotFoundException("Posicion no encontrada")
            }
        }
        return fragmentList[position]
    }

}