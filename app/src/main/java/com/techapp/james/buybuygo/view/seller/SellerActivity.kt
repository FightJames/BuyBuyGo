package com.techapp.james.buybuygo.view.seller

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Menu
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.view.commonFragment.ExpandableAdapter
import com.techapp.james.buybuygo.view.commonFragment.UserInfoFragment
import com.techapp.james.buybuygo.view.seller.fragment.commodity.CommodityFragment
import com.techapp.james.buybuygo.view.seller.fragment.live.LiveFragment
import com.techapp.james.buybuygo.view.seller.fragment.OrderFragment
import com.techapp.james.buybuygo.view.seller.fragment.uploaded.UploadFragment
import kotlinx.android.synthetic.main.activity_seller.*

class SellerActivity : AppCompatActivity() {
    lateinit var fList: ArrayList<Fragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_seller)

        var commodityFragment = CommodityFragment.newInstance()
        var liveFragment = LiveFragment.newInstance()
        var orderFragment = OrderFragment.newInstance()
        var uploadFragment = UploadFragment.newInstance()
        var userInfoFragment = UserInfoFragment.newInstance(ExpandableAdapter.SELLER_MODE)
        fList = arrayListOf<Fragment>(
            commodityFragment,
            liveFragment as Fragment,
            orderFragment,
            uploadFragment,
            userInfoFragment
        )

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {

        return super.onPrepareOptionsMenu(menu)
    }
}
