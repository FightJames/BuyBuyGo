package com.techapp.james.buybuygo.view.buyer.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.techapp.james.buybuygo.R
import kotlinx.android.synthetic.main.buyer_activity_payment.*
import timber.log.Timber

class PaymentActivity : AppCompatActivity() {
    companion object {
        val PAYMENT_WEB_CONTENT = "PAYMENT_WEB_CONTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buyer_activity_payment)

        var paymentWebContent = intent.getStringExtra(PAYMENT_WEB_CONTENT)
        Timber.d("Payment URL $paymentWebContent")
        payWebView.settings.setJavaScriptEnabled(true)
        //this function map to JS's function window.open()
        payWebView.settings.setJavaScriptCanOpenWindowsAutomatically(false)
        payWebView.isVerticalScrollBarEnabled = false
        payWebView.isHorizontalScrollBarEnabled = false
        payWebView.settings.setAppCacheEnabled(false);
        payWebView.setWebViewClient(WebViewClient());
        payWebView.loadData(paymentWebContent, "text/html", null)
    }
}
