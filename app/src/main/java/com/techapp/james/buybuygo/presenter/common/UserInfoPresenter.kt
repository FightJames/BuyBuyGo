package com.techapp.james.buybuygo.presenter.common

import com.google.gson.JsonObject
import com.techapp.james.buybuygo.model.data.Recipients
import com.techapp.james.buybuygo.model.retrofitManager.RayCommon
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.commonFragment.UserInfoFragment
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class UserInfoPresenter {
    var fragment: UserInfoFragment
    var rayCommon: RayCommon

    constructor(fragment: UserInfoFragment) {
        this.fragment = fragment
        rayCommon = RetrofitManager.getInstance().getRayCommon()
    }

    fun createRecipients(recipients: Recipients): Single<Response<ResponseBody>> {
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
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        return rayCommon.createRecipients(Configure.RAY_ACESS_TOKEN, requestBody)

    }
}