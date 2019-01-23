package com.techapp.james.buybuygo.presenter

import android.app.Activity
import android.content.Intent
import com.techapp.james.buybuygo.view.buyer.BuyerActivity
import com.techapp.james.buybuygo.view.seller.SellerActivity

class ChoosePresenter {
    val activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun chooseBuyer() {
        var i = Intent(activity, BuyerActivity::class.java)
        activity.startActivity(i)
    }

    fun chooseSeller() {
        var i = Intent(activity, SellerActivity::class.java)
        activity.startActivity(i)
    }
}