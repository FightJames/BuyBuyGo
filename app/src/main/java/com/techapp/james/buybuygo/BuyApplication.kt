package com.techapp.james.buybuygo

import android.app.Application
import timber.log.Timber

class BuyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}