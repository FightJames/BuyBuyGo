package com.techapp.james.buybuygo.presenter.common

import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RayCommon
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.commonFragment.AreaParameter
import com.techapp.james.buybuygo.view.commonFragment.UserInfoView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber

class UserInfoPresenter {
    var rayCommon: RayCommon
    var rayBuyer: RayBuyer
    var view: UserInfoView
    var rayToken: String

    constructor(view: UserInfoView) {
        this.view = view
        rayCommon = RetrofitManager.getInstance().getRayCommon()
        rayBuyer = RetrofitManager.getInstance().getRayBuyer()
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun createRecipients(recipients: Recipient, complete: (() -> Unit)? = null) {
        var jsonObject = convertRecipientToJSON(recipients)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        var singleRecipient = rayBuyer.createRecipients(rayToken, requestBody)

        singleRecipient.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }
            .doOnSuccess {
                complete?.invoke()
                getBuyerUser()
            }
            .doOnError {
            }.subscribe()
    }

    fun deleteRecipients(recipients: Recipient) {
        var jsonObject = JSONObject()
        var recipientsArray = JSONArray()
        recipientsArray.put(recipients.id)

        jsonObject.put("recipients", recipientsArray)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        var singleDelete = rayBuyer.deleteRecipient(rayToken, requestBody)

        singleDelete.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }
            .doOnSuccess {
                //                view.isLoadWholeView(false)
                getBuyerUser()
                it.body()?.let {
                    view.showRequestMessage(it.response)
                }
                it.errorBody()?.let {
                    view.showRequestMessage(it.string())
                }
            }
            .doOnError {
                Timber.d("error delete " + it.message)
                view.showRequestMessage(it.message.toString())
            }
            .subscribe()
    }

    fun getBuyerUser() {
        var singleUser = rayCommon.getUser(rayToken)
        singleUser.zipWith(
            rayBuyer.getRecipients(rayToken),
            object :
                BiFunction<Response<Wrapper<User>>, Response<Wrapper<ArrayList<Recipient>>>, User> {
                override fun apply(
                    t1: Response<Wrapper<User>>,
                    t2: Response<Wrapper<ArrayList<Recipient>>>
                ): User {
                    var userWrapper = t1.body()!!
                    if (t2.isSuccessful) {
                        var recipientsWrapper = t2.body()!!
                        if (recipientsWrapper.result) {
                            var user = userWrapper.response
                            var recipients = recipientsWrapper.response
                            user.recipients = recipients
                            Timber.d("ok " + user.name + " " + user.email)
                            return user
                        }
                    }
                    userWrapper = t1.body()!!
                    var user = userWrapper.response
                    return user
                }
            }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }
            .doOnSuccess {
                Configure.user = it
                view.isLoadWholeView(false)
                view.updateUserInfo()
            }
            .subscribe()
    }

    fun modifyRecipient(recipient: Recipient) {
        var jsonObject = convertRecipientToJSON(recipient)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        var singleRecipient = rayBuyer.updateRecipient(rayToken, recipient.id, requestBody)
        singleRecipient.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }
            .doOnSuccess {
                it.body()?.let {
                    view.showRequestMessage(it.response)
                }
                getBuyerUser()
            }
            .doOnError {
            }.subscribe()
    }

    fun getCountryWrappers() {
        var singleCountryWrapper = rayCommon.getCountryWrapper(rayToken)

        singleCountryWrapper.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }
            .doOnSuccess {
                view.isLoadWholeView(false)
                it.body()?.let {
                    AreaParameter.countryWrapperList = it.response
                }
            }.doOnError {
                Timber.d("error ${it.message}")
            }.subscribe()

//        contryWrapper.subscribeOn(Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSuccess {
//                it.body()?.let {
//                    var countryList = it.response
//                    countryList.forEach {
//                        Timber.d(it.country)
//                    }
//                }
//                Timber.d("error body ${it.errorBody()?.string()}")
//            }.doOnError {
//                Timber.d("error ${it.message}")
//            }.subscribe()
    }

    fun convertRecipientToJSON(recipients: Recipient): JSONObject {
        var jsonObject = JSONObject()
        jsonObject.put("name", recipients.name)
        var phoneObject = JSONObject()
        var phone = recipients.phone
        phoneObject.put("phone_code", phone.code)
        phoneObject.put("phone_number", phone.number)
        jsonObject.put("phone", phoneObject)
        var address = recipients.address
        var addressObject = JSONObject()
        addressObject.put("country_code", address.countryCode)
        addressObject.put("post_code", address.postCode)
        addressObject.put("city", address.city)
        addressObject.put("district", address.district)
        addressObject.put("others", address.others)
        jsonObject.put("address", addressObject)
        return jsonObject
    }
}