package com.techapp.james.buybuygo.view.buyer.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.view.buyer.fragment.live.LiveFragment
import com.techapp.james.buybuygo.view.buyer.fragment.order.OrderFragment
import com.techapp.james.buybuygo.view.commonFragment.ExpandableAdapter
import com.techapp.james.buybuygo.view.commonFragment.UserInfoFragment
import kotlinx.android.synthetic.main.activity_buyer.*

class BuyerActivity : AppCompatActivity() {

    lateinit var fList: ArrayList<Fragment>
    var liveFragment = LiveFragment.newInstance()
    var orderFragment = OrderFragment.newInstance()
    var userInfoFragment = UserInfoFragment.newInstance(ExpandableAdapter.BUYER_MODE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//        actionBar.title = actionBar.title.toString() + " Buyer"
        supportActionBar?.let {
            it.title = it.title.toString() + " Buyer"
        }
        fList = arrayListOf<Fragment>(liveFragment, orderFragment, userInfoFragment)

        viewPagerRoot.adapter = ContentPagerAdapter(
            fList as List<Fragment>,
            supportFragmentManager
        )

        viewPagerRoot.setOffscreenPageLimit(0)

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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        orderFragment.onNewIntent()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure to leave?")
            .setPositiveButton(R.string.ok, { dialog, which ->
                this@BuyerActivity.finish()
            })
            .setNegativeButton(R.string.cancel, { dialog, which ->
                dialog.cancel()
            })
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
