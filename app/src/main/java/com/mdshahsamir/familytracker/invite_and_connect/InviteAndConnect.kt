package com.mdshahsamir.familytracker.invite_and_connect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mdshahsamir.familytracker.R
import com.mdshahsamir.familytracker.invite_and_connect.connect.ConnectMembers
import com.mdshahsamir.familytracker.invite_and_connect.invite.InviteMembers


class InviteAndConnect : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter : ViewPagerAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite_and_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager2>(R.id.pager)
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when(position){
                0-> "Connect"
                1->"Invite"
                else->"Requests"
            }
            //tab.setIcon(if (position == 0) R.drawable.connect_member_icon else R.drawable.invite_member_icon)
        }.attach()
    }

}

