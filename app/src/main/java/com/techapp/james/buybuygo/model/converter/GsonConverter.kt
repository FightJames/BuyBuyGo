package com.techapp.james.buybuygo.model.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.techapp.james.buybuygo.model.data.common.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.common.UserStatus

object GsonConverter {
    var gson = GsonBuilder().create()
    fun convertJsonToWrapperString(json: String): Wrapper<String> {
//        var type = object : TypeToken<Wrapper<String>>() {}.type
        var wrapperString =
//            gson.fromJson<Wrapper<String>>(json, type)
            gson.fromJson<Wrapper<String>>(json)
        return wrapperString
    }

    fun convertJsonToWrapperUserStatus(json: String): Wrapper<UserStatus> {
//        var type = object : TypeToken<Wrapper<String>>() {}.type
        var wrapperUserStatus =
//            gson.fromJson<Wrapper<String>>(json, type)
            gson.fromJson<Wrapper<UserStatus>>(json)
        return wrapperUserStatus
    }


    inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}