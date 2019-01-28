package com.techapp.james.buybuygo.view.buyer.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ContentPagerAdapter(val fList: List<Fragment>, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(p0: Int): Fragment = fList[p0]

    override fun getCount(): Int = fList.size

}