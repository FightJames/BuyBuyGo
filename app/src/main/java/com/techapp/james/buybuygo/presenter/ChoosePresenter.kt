package com.techapp.james.buybuygo.presenter

import android.app.Activity
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import retrofit2.Response

class ChoosePresenter {
    val view: View
    var rayToken: String

    constructor(view: View) {
        this.view = view
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun chooseBuyer(): Single<User> {
        var common = RetrofitManager.getInstance().getRayCommon()
        var buyer = RetrofitManager.getInstance().getRayBuyer()
        return common.getUser(rayToken)
            .zipWith(
                buyer.getRecipients(rayToken),
                object :
                    BiFunction<Response<Wrapper<User>>, Response<Wrapper<ArrayList<Recipient>>>, User> {
                    override fun apply(
                        t1: Response<Wrapper<User>>,
                        t2: Response<Wrapper<ArrayList<Recipient>>>
                    ): User {
                        var user = t1.body()!!.response
                        if (t2.body() == null) null else user.recipients = t2.body()!!.response
                        return user
                    }
                }).doOnSuccess {
                Configure.user = it
            }

    }

    fun chooseSeller(): Single<Response<Wrapper<User>>> {
        var common = RetrofitManager.getInstance().getRayCommon()
        return common.getUser(rayToken).doOnSuccess {
            Configure.user = it.body()!!.response
        }
    }
}