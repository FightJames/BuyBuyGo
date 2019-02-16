package com.techapp.james.buybuygo.presenter

import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.login.LoginView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

class LoginPresenter {
    val view: LoginView

    constructor(view: LoginView) {
        this.view = view
    }

    fun onLoginSuccess(fbToken: String, expirationDate: String) {
        SharePreference.getInstance().saveFBToken(fbToken)
        //bug in here when network is slow, It lead other page cant fetch backend item data.
        SharePreference.getInstance().saveExpDate(expirationDate)
        SharePreference.getInstance().saveRayToken("Bearer " + fbToken)
        loginBackEnd(fbToken, expirationDate)
//        var t = Test()
//        t.testRecordUser(activity.applicationContext, t::testUpCommodity)
//        t.testUpCommodity(activity.applicationContext)
//        t.testRecordUser(activity.applicationContext, t::testGetItems)
    }

    private fun loginBackEnd(
        fbToken: String,
        expirationDate: String
    ) {
        var root = JSONObject()
        root.put("expirationDate", expirationDate)
        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())
        var rayCommon = RetrofitManager.getInstance().getRayCommon()
        var singleRecordUser = rayCommon.recordUser("Bearer " + fbToken, requestBody)

        singleRecordUser.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
                view.intentToChoose()
            }
            .onErrorReturn({ error ->
                view.showMessage(error.message.toString())
                var jsonObject = JSONObject()
                jsonObject.put("expiresIn", "error!")
                var body = ResponseBody.create(
                    MediaType.parse("application/json"),
                    jsonObject.toString()
                )
                Timber.d("error item return")
//                                 408 is timeout code
                Response.error<ResponseBody>(408, body)
            })
            .doOnEvent { body, throwable ->
                //onErrorItemReturn will emmit a data which not include throwable to this
                Timber.d("call")
                view.isLoad(false)
            }
            .subscribe()
    }
}