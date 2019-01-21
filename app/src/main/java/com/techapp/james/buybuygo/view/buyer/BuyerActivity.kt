package com.techapp.james.buybuygo.view.buyer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.techapp.james.buybuygo.R
import kotlinx.android.synthetic.main.activity_buyer.*

class BuyerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)
        val myHtmlString = "<html><body>" +
                "<iframe src=\"https://www.facebook.com/plugins/video.php?href=https%3A%2F%2Fwww.facebook.com%2FDoctorKoWJ%2Fvideos%2F315693975739074%2F&show_text=0&width=100%\" width=\"100%\" height=\"100%\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\" allowFullScreen=\"true\"></iframe></ body></html >"
//                .javaScriptEnabled = true
//        fbLiveWebView.loadData(myHtmlString, "text/html", null)
        fbLiveWebView.settings.setJavaScriptEnabled(true);
        fbLiveWebView.settings.setSupportZoom(true);
        fbLiveWebView.settings.setBuiltInZoomControls(true);
        fbLiveWebView.settings.setJavaScriptCanOpenWindowsAutomatically(true);
        fbLiveWebView.settings.setAppCacheEnabled(true);
        fbLiveWebView.setWebViewClient(WebViewClient());
        fbLiveWebView.loadData(myHtmlString, "text/html", null)
    }
}
