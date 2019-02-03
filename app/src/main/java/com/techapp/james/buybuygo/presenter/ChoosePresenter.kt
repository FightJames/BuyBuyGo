package com.techapp.james.buybuygo.presenter

import android.app.Activity
import android.content.Intent
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.buyer.BuyerActivity
import com.techapp.james.buybuygo.view.seller.SellerActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_choose.*
import retrofit2.Response

class ChoosePresenter {
    val view: View

    constructor(view: View) {
        this.view = view
    }

    fun chooseBuyer(): Single<User> {
        var common = RetrofitManager.getInstance().getRayCommon()
        var buyer = RetrofitManager.getInstance().getRayBuyer()
        return common.getUser(Configure.RAY_ACCESS_TOKEN)
            .zipWith(
                buyer.getRecipients(Configure.RAY_ACCESS_TOKEN),
                object :
                    BiFunction<Response<Wrapper<User>>, Response<Wrapper<ArrayList<Recipient>>>, User> {
                    override fun apply(
                        t1: Response<Wrapper<User>>,
                        t2: Response<Wrapper<ArrayList<Recipient>>>
                    ): User {
                        var user = t1.body()!!.response
                        var recipients = if (t2.body() == null) null else t2.body()!!.response
                        user.recipients = recipients
                        return user
                    }
                })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Configure.user = it
            }
    }

    fun chooseSeller(): Single<Response<Wrapper<User>>> {
        var common = RetrofitManager.getInstance().getRayCommon()
        return common.getUser(Configure.RAY_ACCESS_TOKEN)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                it.body()?.let {
                    Configure.user = it.response
                }
            }
    }
}