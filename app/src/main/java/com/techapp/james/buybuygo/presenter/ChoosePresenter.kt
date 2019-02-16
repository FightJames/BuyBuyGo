package com.techapp.james.buybuygo.presenter

import android.app.Activity
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.choose.ChooseView
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber

class ChoosePresenter {
    val view: ChooseView
    var rayToken: String

    constructor(view: ChooseView) {
        this.view = view
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun chooseBuyer() {
        var common = RetrofitManager.getInstance().getRayCommon()
        var buyer = RetrofitManager.getInstance().getRayBuyer()
        common.getUser(rayToken)
            .zipWith(
                buyer.getRecipients(rayToken),
                object :
                    BiFunction<Response<Wrapper<User>>, Response<Wrapper<ArrayList<Recipient>>>, User> {
                    override fun apply(
                        t1: Response<Wrapper<User>>,
                        t2: Response<Wrapper<ArrayList<Recipient>>>
                    ): User {
//                        Timber.d("error " + t1.errorBody())
                        var user = t1.body()!!.response
                        if (t2.body() == null) null else user.recipients = t2.body()!!.response
                        return user
                    }
                }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                Configure.user = it
                view.isLoad(false)
                view.intentToBuyer()
            }.subscribe()
    }

    fun chooseSeller() {
        var common = RetrofitManager.getInstance().getRayCommon()
        common.getUser(rayToken)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
                Configure.user = it.body()!!.response
                view.intentToSeller()
            }.subscribe()
    }
}