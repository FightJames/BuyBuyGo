package com.techapp.james.buybuygo.view.seller.activity.seller

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.view.commonFragment.ExpandableAdapter
import com.techapp.james.buybuygo.view.commonFragment.UserInfoFragment
import com.techapp.james.buybuygo.view.seller.fragment.channelRecord.ChannelRecordFragment
import com.techapp.james.buybuygo.view.seller.fragment.commodity.CommodityFragment
import com.techapp.james.buybuygo.view.seller.fragment.live.LiveFragment
import com.techapp.james.buybuygo.view.seller.fragment.order.OrderFragment
import kotlinx.android.synthetic.main.activity_seller.*


class SellerActivity : AppCompatActivity() {
    lateinit var fList: ArrayList<Fragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_seller)
//        actionBar.title = actionBar.title.toString() + " Seller"

        supportActionBar?.let {
            it.title = it.title.toString() + " Seller"
        }
        var commodityFragment = CommodityFragment.newInstance()
        var liveFragment = LiveFragment.newInstance()
        var orderFragment = OrderFragment.newInstance()
        var channelRecordFragment = ChannelRecordFragment.newInstance()
        var userInfoFragment = UserInfoFragment.newInstance(ExpandableAdapter.SELLER_MODE)
        fList = arrayListOf<Fragment>(
            commodityFragment,
            liveFragment as Fragment,
            channelRecordFragment,
            orderFragment,
            userInfoFragment
        )

        viewPagerRoot.adapter =
            ContentPagerAdapter(
                fList as List<Fragment>,
                supportFragmentManager
            )
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

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure to leave?")
            .setPositiveButton(R.string.ok, { dialog, which ->
                this@SellerActivity.finish()
            })
            .setNegativeButton(R.string.cancel, { dialog, which ->
                dialog.cancel()
            })
        val dialog = builder.create()
        dialog.show()
    }
}
