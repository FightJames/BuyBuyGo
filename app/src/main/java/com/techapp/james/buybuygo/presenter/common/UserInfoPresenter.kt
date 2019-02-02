package com.techapp.james.buybuygo.presenter.common

import com.techapp.james.buybuygo.model.data.CountryWrapper
import com.techapp.james.buybuygo.model.data.Recipient
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RayCommon
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.commonFragment.UserInfoFragment
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
    var view: View

    constructor(view: View) {
        this.view = view
        rayCommon = RetrofitManager.getInstance().getRayCommon()
        rayBuyer = RetrofitManager.getInstance().getRayBuyer()
    }

    fun createRecipients(recipients: Recipient): Single<Response<ResponseBody>> {
        var jsonObject = convertRecipientToJSON(recipients)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        return rayBuyer.createRecipients(Configure.RAY_ACCESS_TOKEN, requestBody)
    }

    fun deleteRecipients(recipients: Recipient): Single<Response<Wrapper<String>>> {
        var jsonObject = JSONObject()
        var recipientsArray = JSONArray()
        recipientsArray.put(recipients.id)

        jsonObject.put("recipients", recipientsArray)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        return rayBuyer.deleteRecipient(Configure.RAY_ACCESS_TOKEN, requestBody)
    }

    fun getBuyerUser(): Single<User> {
        var uSingle = rayCommon.getUser(Configure.RAY_ACCESS_TOKEN)
        return uSingle.zipWith(
            rayBuyer.getRecipients(Configure.RAY_ACCESS_TOKEN),
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
            })
    }

    fun modifyRecipient(recipient: Recipient): Single<Response<Wrapper<String>>> {
        var jsonObject = convertRecipientToJSON(recipient)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        return rayBuyer.updateRecipient(Configure.RAY_ACCESS_TOKEN, recipient.id, requestBody)
    }

    fun getCountryWrappers(): Single<Response<Wrapper<ArrayList<CountryWrapper>>>> {
        var contryWrapper = rayCommon.getCountryWrapper(Configure.RAY_ACCESS_TOKEN)
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
        return contryWrapper
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