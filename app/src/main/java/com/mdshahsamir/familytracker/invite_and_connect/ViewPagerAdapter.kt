package com.mdshahsamir.familytracker.invite_and_connect

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mdshahsamir.familytracker.invite_and_connect.connect.ConnectMembers
import com.mdshahsamir.familytracker.invite_and_connect.invite.InviteMembers
import com.mdshahsamir.familytracker.invite_and_connect.invite_requests.InviteRequests

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> ConnectMembers()
            1-> InviteMembers()
            else-> InviteRequests()
        }
    }
}