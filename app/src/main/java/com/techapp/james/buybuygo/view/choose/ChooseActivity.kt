package com.techapp.james.buybuygo.view.choose

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.presenter.ChoosePresenter
import com.techapp.james.buybuygo.view.BaseActivity
import kotlinx.android.synthetic.main.activity_choose.*

class ChooseActivity : BaseActivity() {
    var choosePresenter: ChoosePresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        init()
    }

    fun init() {
        Glide.with(this)
                .load(R.drawable.buyer_seller)
                .into(backgroundImageView)
        choosePresenter = ChoosePresenter(this)
        buyerTextView.setOnClickListener {
            choosePresenter!!.chooseBuyer()
        }
        sellerTextView.setOnClickListener {
            choosePresenter!!.chooseSeller()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        choosePresenter = null
    }
}
