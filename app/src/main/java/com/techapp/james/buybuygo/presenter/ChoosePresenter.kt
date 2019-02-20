package com.techapp.james.buybuygo.presenter

import com.techapp.james.buybuygo.model.converter.GsonConverter
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.common.User
import com.techapp.james.buybuygo.model.data.common.UserStatus
import com.techapp.james.buybuygo.model.data.common.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.choose.ChooseView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

class ChoosePresenter {
    val view: ChooseView
    var rayToken: String

    var common = RetrofitManager.getInstance().getRayCommon()
    var buyer = RetrofitManager.getInstance().getRayBuyer()

    constructor(view: ChooseView) {
        this.view = view
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun chooseBuyer() {
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

    fun getUserStatus() {
        var singleUserState = common.getUserStatus(rayToken)
        singleUserState.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }.doOnSuccess {
                view.isLoad(false)
                it.body()?.let {
                    var string = it.string()
                    var jsonObject = JSONObject(string)
                    var judge = jsonObject.get("result")
                    Timber.d("result judge " + judge)
                    if (judge.toString() == "true") {
                        var userStatus =
                            GsonConverter.convertJsonToWrapperUserStatus(jsonObject.get("response").toString())
                        Configure.userState = userStatus
                        Timber.d("result " + "autoIntent")
                        Timber.d("result " + userStatus.host)
                        view.autoIntent(userStatus)

                    } else {

                        var string = jsonObject.get("response").toString()
                        Timber.d("result " + string)
                    }
                }
            }.subscribe()
    }
}