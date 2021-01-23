package com.mdshahsamir.familytracker.invite_and_connect

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mdshahsamir.familytracker.invite_and_connect.connect.ConnectMembers
import com.mdshahsamir.familytracker.invite_and_connect.invite.InviteMembers

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) ConnectMembers() else InviteMembers()
    }
}