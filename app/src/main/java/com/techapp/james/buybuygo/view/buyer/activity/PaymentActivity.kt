package com.techapp.james.buybuygo.view.buyer.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.buyer_activity_payment.*
import timber.log.Timber
import android.support.v4.content.ContextCompat.startActivity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.R
import android.net.Uri


class PaymentActivity : AppCompatActivity() {
    companion object {
        val PAYMENT_WEB_CONTENT = "PAYMENT_WEB_CONTENT"
        val PAYMENT_WAY = "PAYMENT_WAY"
        val ALLPAY = 1
        val PAYPAL = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.techapp.james.buybuygo.R.layout.buyer_activity_payment)

        var paymentWebContent = intent.getStringExtra(PAYMENT_WEB_CONTENT)
        var payWay = intent.getIntExtra(PAYMENT_WAY, 0)
        Timber.d("Payment URL $payWay")
        payWebView.settings.setJavaScriptEnabled(true)
        //this function map to JS's function window.open()
        payWebView.settings.setJavaScriptCanOpenWindowsAutomatically(true)
        payWebView.isVerticalScrollBarEnabled = false
        payWebView.isHorizontalScrollBarEnabled = false
        payWebView.settings.setAppCacheEnabled(false);
        payWebView.webViewClient = WebViewClient()
        when (payWay) {
            PAYPAL -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentWebContent))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setPackage("com.android.chrome")
                try {
                    startActivity(intent)
                } catch (ex: ActivityNotFoundException) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null)
                    startActivity(intent)
                }

            }
            ALLPAY -> {
                payWebView.loadData(paymentWebContent, "text/html", null)
//                payWebView.loadData(string, "text/html", null)
            }
        }
//        payWebView.loadUrl("https://www.sandbox.paypal.com/cgi-bin/webscr?order_id%5B0%5D=6&ClintBackURL=https%3A%2F%2Ffacebookoptimizedlivestreamsellingsystem.rayawesomespace.space%2F&cmd=_xclick&business=buybuybuygogo%40gmail.com&return=https%3A%2F%2Ffacebookoptimizedlivestreamsellingsystem.rayawesomespace.space%2F&cancel_return=&notify_url=https%3A%2F%2F7297f315.ngrok.io%2Fapi%2Fpaypallistener&item_name=1549379242ytxiOi&amount=14000&currency_code=TWD&custom=1550456615zkrt7g")
    }
}
