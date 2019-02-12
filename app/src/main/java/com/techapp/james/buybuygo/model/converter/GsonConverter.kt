package com.techapp.james.buybuygo.model.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.techapp.james.buybuygo.model.data.Wrapper

object GsonConverter {
    var gson = GsonBuilder().create()
    fun convertJsonToWrapperString(json: String): Wrapper<String> {
        var type = object : TypeToken<Wrapper<String>>() {}.type
        var wrapperString =
            gson.fromJson<Wrapper<String>>(json, type)
//        Gson().fromJson(json,Wrapper<String>::class.java)
        return wrapperString
    }

    inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}