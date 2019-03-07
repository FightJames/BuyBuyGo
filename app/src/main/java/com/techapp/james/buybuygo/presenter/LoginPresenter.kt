package com.techapp.james.buybuygo.presenter

import com.techapp.james.buybuygo.model.converter.GsonConverter
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.login.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

class LoginPresenter {
    val view: LoginView
    val sharePreference = SharePreference.getInstance()

    constructor(view: LoginView) {
        this.view = view
        var exp = sharePreference.getExpDate()
        if (!exp.equals("")) {
            var expMillTime = exp.toLong()
            if (System.currentTimeMillis() < expMillTime) {
                Timber.d("toke ${sharePreference.getRayToken()}")
                view.intentToChoose()
                view.finish()
            }
        }
    }

    fun onLoginSuccess(fbToken: String, expirationDate: String) {
        loginBackEnd(fbToken)
//        Timber.d("FB Token $fbToken")
//        sharePreference.saveFBToken(fbToken)
        //bug in here when network is slow, It lead other page cant fetch backend item data.
//        var t = Test()
//        t.testRecordUser(activity.applicationContext, t::testUpCommodity)
//        t.testUpCommodity(activity.applicationContext)
//        t.testRecordUser(activity.applicationContext, t::testGetItems)
    }

    private fun loginBackEnd(
        fbToken: String
    ) {
        Timber.d("FB token " + fbToken)
//        var root = JSONObject()
//        root.put("expirationDate", expirationDate)
//        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())
        var rayCommon = RetrofitManager.getInstance().getRayCommon()
        var singleRecordUser = rayCommon.recordUser("Bearer " + fbToken)

        singleRecordUser.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
//                Timber.d("response " + it.body()?.string())
                if (it.code() == 200) {
                    it.body()?.let {
//                        Timber.d("response " + it.string())
                        var accessToken = GsonConverter.convertJsonToAccessToken(it.string())
//                        Timber.d("hello success")
                        var expTime = accessToken.exp.toLong() * 100
                        var exp = System.currentTimeMillis() + expTime
                        sharePreference.saveExpDate(exp.toString())
                        sharePreference.saveRayToken("Bearer " + accessToken.token)
                        view.intentToChoose()
                        view.finish()
                    }
                }

            }
            .onErrorReturn({ error ->
                view.showMessage(error.message.toString())
                Timber.d("error " + error.message.toString())
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