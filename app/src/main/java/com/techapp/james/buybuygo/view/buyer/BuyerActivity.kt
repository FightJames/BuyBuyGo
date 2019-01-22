package com.techapp.james.buybuygo.view.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.view.buyer.fragment.LiveFragment
import kotlinx.android.synthetic.main.activity_buyer.*

class BuyerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)
        var liveFragment = LiveFragment.newInstance()
        var frTrasaction = this.supportFragmentManager.beginTransaction()
        frTrasaction.replace(R.id.root, liveFragment)
//        frTrasaction.addToBackStack("Live")
        frTrasaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
