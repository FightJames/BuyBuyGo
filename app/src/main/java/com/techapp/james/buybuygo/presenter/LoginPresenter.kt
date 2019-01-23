package com.techapp.james.buybuygo.presenter

import android.app.Activity
import android.content.Intent
import com.techapp.james.buybuygo.model.retrofitManager.Test
import com.techapp.james.buybuygo.view.choose.ChooseActivity

class LoginPresenter {
    val activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun onLoginSuccess(fbToken: String, expirationDate: String) {
        Configure.FB_ACESS_TOKEN = fbToken
        Configure.FB_EXPIRATIONDATE = expirationDate
        var i = Intent(activity, ChooseActivity::class.java)
        activity.startActivity(i)
//        var t = Test()
//        t.testRecordUser()
    }
}