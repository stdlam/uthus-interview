package com.practices.alebeer.other.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practices.alebeer.ui.main.beer.BeerFragment
import com.practices.alebeer.ui.main.favorite.FavFragment

class MainPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                BeerFragment()
            }
            else -> {
                FavFragment()
            }
        }
    }

}