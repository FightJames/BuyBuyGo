package com.techapp.james.buybuygo.view.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.view.buyer.fragment.ContentPagerAdapter
import com.techapp.james.buybuygo.view.buyer.fragment.LiveFragment
import com.techapp.james.buybuygo.view.commonFragment.ExpandableAdapter
import com.techapp.james.buybuygo.view.commonFragment.UserInfoFragment
import com.techapp.james.buybuygo.view.seller.fragment.OrderFragment
import kotlinx.android.synthetic.main.activity_buyer.*

class BuyerActivity : AppCompatActivity() {

    lateinit var fList: ArrayList<Fragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)
        var liveFragment = LiveFragment.newInstance()
        var orderFragment = OrderFragment.newInstance()
        var userInfoFragment = UserInfoFragment.newInstance(ExpandableAdapter.BUYER_MODE)
        fList = arrayListOf<Fragment>(liveFragment, orderFragment, userInfoFragment)

        viewPagerRoot.adapter = ContentPagerAdapter(fList as List<Fragment>, supportFragmentManager)
        viewPagerRoot.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                bottomNavigationView.menu.getItem(p0).isChecked = true
            }

        })
        bottomNavigationView.setOnNavigationItemSelectedListener {
            viewPagerRoot.currentItem = it.order
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
