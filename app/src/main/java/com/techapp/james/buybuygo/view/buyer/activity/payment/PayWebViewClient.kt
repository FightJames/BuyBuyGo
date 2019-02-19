package com.techapp.james.buybuygo.view.buyer.activity.payment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import timber.log.Timber

class PayWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Timber.d(123.toString() + url)
        if (url.equals("http://buybuygo.tech.tw/")) {
            view?.let {
                (it.context as Activity).finish()
            }
            return true
        }
        return false
    }
}