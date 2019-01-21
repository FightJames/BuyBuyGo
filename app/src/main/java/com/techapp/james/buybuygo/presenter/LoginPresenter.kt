package com.techapp.james.buybuygo.presenter

import android.app.Activity
import android.content.Intent
import com.techapp.james.buybuygo.view.choose.ChooseActivity

class LoginPresenter {
    val activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun onLoginSuccess(fbToken: String) {
        Configure.FB_ACESS_TOKEN = fbToken
        var i = Intent(activity, ChooseActivity::class.java)
        activity.startActivity(i)
    }
}