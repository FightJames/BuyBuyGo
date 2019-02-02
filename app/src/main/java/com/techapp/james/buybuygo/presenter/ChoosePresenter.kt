package com.techapp.james.buybuygo.presenter

import android.app.Activity
import android.content.Intent
import android.view.View
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.view.buyer.BuyerActivity
import com.techapp.james.buybuygo.view.seller.SellerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_choose.*
import retrofit2.Response

class ChoosePresenter {
    val activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun chooseBuyer() {
        var common = RetrofitManager.getInstance().getRayCommon()
        var buyer = RetrofitManager.getInstance().getRayBuyer()
        common.getUser(Configure.RAY_ACCESS_TOKEN)
                .zipWith(buyer.getRecipients(Configure.RAY_ACCESS_TOKEN), object : BiFunction<Response<Wrapper<User>>, Response<Wrapper<ArrayList<Recipient>>>, User> {
                    override fun apply(t1: Response<Wrapper<User>>, t2: Response<Wrapper<ArrayList<Recipient>>>): User {
                        var user = t1.body()!!.response
                        var recipients = if (t2.body() == null) null else t2.body()!!.response
                        user.recipients = recipients
                        return user
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    activity.sellerTextView.visibility = View.INVISIBLE
                    activity.buyerTextView.visibility = View.INVISIBLE
                    activity.loadUserDataprogressBar.visibility = View.VISIBLE
                }
                .doOnSuccess {
                    var i = Intent(activity, BuyerActivity::class.java)
                    activity.startActivity(i)
                    Configure.user = it
                }.subscribe()
    }

    fun chooseSeller() {
        var common = RetrofitManager.getInstance().getRayCommon()
        common.getUser(Configure.RAY_ACCESS_TOKEN)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    activity.sellerTextView.visibility = View.INVISIBLE
                    activity.buyerTextView.visibility = View.INVISIBLE
                    activity.loadUserDataprogressBar.visibility = View.VISIBLE
                }
                .doOnSuccess {
                    it.body()?.let {
                        Configure.user = it.response
                    }
                    var i = Intent(activity, SellerActivity::class.java)
                    activity.startActivity(i)
                }.subscribe()

    }
}