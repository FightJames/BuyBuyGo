package com.techapp.james.buybuygo.model.sharePreference

import android.content.Context

class SharePreference {
    val DATA = "data"
    val FB_TOKEN = "fb_token"
    val RAY_TOKEN = "ray_token"
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

    fun saveRayToken(rayToken: String) {
        var sharePreference = context.getSharedPreferences(DATA, 0)
        sharePreference.edit()
            .putString(rayToken, RAY_TOKEN)
            .commit()
    }

    fun saveFBToken(fbToken: String) {
        var sharePreference = context.getSharedPreferences(DATA, 0)
        sharePreference.edit()
            .putString(fbToken, FB_TOKEN)
            .commit()
    }
}
