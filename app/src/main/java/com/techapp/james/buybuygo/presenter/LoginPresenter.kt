package com.techapp.james.buybuygo.presenter

import android.app.Activity
import android.content.Intent
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.choose.ChooseActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class LoginPresenter {
    val view: View

    constructor(view: View) {
        this.view = view
    }

    fun onLoginSuccess(fbToken: String, expirationDate: String): Single<Response<ResponseBody>> {
        Configure.FB_ACCESS_TOKEN = fbToken
        Configure.FB_EXPIRATIONDATE = expirationDate
        SharePreference.getInstance().saveFBToken(fbToken)
        //bug in here when network is slow, It lead other page cant fetch backend item data.
        return loginBackEnd()
//        var t = Test()
//        t.testRecordUser(activity.applicationContext, t::testUpCommodity)
//        t.testUpCommodity(activity.applicationContext)
//        t.testRecordUser(activity.applicationContext, t::testGetItems)
    }

    private fun loginBackEnd(): Single<Response<ResponseBody>> {
        var root = JSONObject()
        root.put("expirationDate", Configure.FB_EXPIRATIONDATE)
        //Todo stroe fb token to sharePreference
        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())
        var rayCommon = RetrofitManager.getInstance().getRayCommon()

        var result = rayCommon.recordUser("Bearer " + Configure.FB_ACCESS_TOKEN, requestBody)
        return result
//        result.subscribeOn(Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe {
//                Configure.RAY_ACCESS_TOKEN = "Bearer ${Configure.FB_ACCESS_TOKEN}"
//                activity.progressBar.visibility = View.VISIBLE
//            }
//            .doOnSuccess {
//                activity.progressBar.visibility = View.GONE
//                var i = Intent(activity, ChooseActivity::class.java)
//                activity.startActivity(i)
//            }
//            .doOnError {}
//            .subscribe()
    }
}