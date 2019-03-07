package com.techapp.james.buybuygo.model.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.techapp.james.buybuygo.model.data.common.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.common.AccessToken
import com.techapp.james.buybuygo.model.data.common.UserStatus
import timber.log.Timber

object GsonConverter {
    var gson = GsonBuilder().create()
    fun convertJsonToWrapperString(json: String): Wrapper<String> {
//        var type = object : TypeToken<Wrapper<String>>() {}.type
        var wrapperString =
//            gson.fromJson<Wrapper<String>>(json, type)
            gson.fromJson<Wrapper<String>>(json)
        return wrapperString
    }

    fun convertJsonToString(json: String): String {
//        var type = object : TypeToken<Wrapper<String>>() {}.type
//            gson.fromJson<Wrapper<String>>(json, type)
        var string =
            gson.fromJson<String>(json)
        return string
    }

    fun convertJsonToWrapperUserStatus(json: String): UserStatus {
//        var type = object : TypeToken<Wrapper<String>>() {}.type
//            gson.fromJson<Wrapper<String>>(json, type)
        var userStatus =
            gson.fromJson<UserStatus>(json)
        return userStatus
    }

    fun convertJsonToAccessToken(json: String): AccessToken {
        var wrapperAccessToken = gson.fromJson<Wrapper<AccessToken>>(json)
//        if(wrapperAccessToken==null){
//            Timber.d("wrapper is null")
//        }
        return wrapperAccessToken.response
    }

    inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}