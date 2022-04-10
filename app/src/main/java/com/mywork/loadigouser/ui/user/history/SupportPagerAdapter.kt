package com.mywork.loadigouser.ui.user.history

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SupportPagerAdapter(
    activity: AppCompatActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = this.fragments.size
    override fun createFragment(position: Int): Fragment = this.fragments[position]
}