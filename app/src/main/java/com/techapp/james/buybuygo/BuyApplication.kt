package com.techapp.james.buybuygo

import android.app.Application
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import timber.log.Timber

class BuyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        SharePreference.init(this)
    }

}