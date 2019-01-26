package com.techapp.james.buybuygo.presenter

import android.app.Activity
import android.content.Intent
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.retrofitManager.Test
import com.techapp.james.buybuygo.view.choose.ChooseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class LoginPresenter {
    val activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun onLoginSuccess(fbToken: String, expirationDate: String) {
        Configure.FB_ACESS_TOKEN = fbToken
        Configure.FB_EXPIRATIONDATE = expirationDate
        //bug in here when network is slow, It lead other page cant fetch backend item data.
        loginBackEnd()
        var i = Intent(activity, ChooseActivity::class.java)
        activity.startActivity(i)
//        var t = Test()
//        t.testRecordUser(activity.applicationContext, t::testUpCommodity)
//        t.testUpCommodity(activity.applicationContext)
//        t.testRecordUser(activity.applicationContext, t::testGetItems)
    }

    private fun loginBackEnd() {
        var root = JSONObject()
        root.put("expirationDate", Configure.FB_EXPIRATIONDATE)
        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())
        var rayCommon = RetrofitManager.getInstance().getRayCommon()
        var result = rayCommon.recordUser("Bearer " + Configure.FB_ACESS_TOKEN, requestBody)
        result.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Configure.RAY_ACESS_TOKEN = "Bearer ${Configure.FB_ACESS_TOKEN}"
                }
                .doOnSuccess {
//                    var test = Test()
//                    test.testGetUser()
                }
                .doOnError {}
                .subscribe()
    }
}