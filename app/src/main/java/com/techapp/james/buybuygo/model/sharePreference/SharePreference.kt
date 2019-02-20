package com.techapp.james.buybuygo.model.sharePreference

import android.content.Context
import com.techapp.james.buybuygo.model.data.common.User
import com.google.gson.Gson


class SharePreference {
    val DATA = "data"
    val FB_TOKEN = "fb_token"
    val RAY_TOKEN = "ray_token"
    var EXP_DATE = "exp_date"
    var USER = "USER"
    var context: Context

    private constructor(context: Context) {
        this.context = context
    }

    companion object {
        private lateinit var instance: SharePreference
        fun init(context: Context) {
            instance = SharePreference(context)
        }

        fun getInstance(): SharePreference {
            return instance
        }
    }

    fun getRayToken(): String {
        var sharePreference = context.getSharedPreferences(DATA, 0)
        return sharePreference.getString(RAY_TOKEN, "")
    }

    fun getExpDate(): String {
        var sharePreference = context.getSharedPreferences(DATA, 0)
        return sharePreference.getString(EXP_DATE, "")
    }

    fun saveExpDate(expDate: String) {
        var sharePreference = context.getSharedPreferences(DATA, 0)
        sharePreference.edit()
            .putString(EXP_DATE, expDate)
            .commit()
    }

    fun saveRayToken(rayToken: String) {
        var sharePreference = context.getSharedPreferences(DATA, 0)
        sharePreference.edit()
            .putString(RAY_TOKEN, rayToken)
            .commit()
    }

    fun saveFBToken(fbToken: String) {
        var sharePreference = context.getSharedPreferences(DATA, 0)
        sharePreference.edit()
            .putString(FB_TOKEN, fbToken)
            .commit()
    }

    fun saveBuyer(u: User) {
        val gson = Gson()
        val serializedObject = gson.toJson(u)
        var sharePreference = context.getSharedPreferences(DATA, 0)
        var editor = sharePreference.edit()
        editor.putString(USER, serializedObject)
        editor.commit()
    }
}
