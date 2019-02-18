package com.techapp.james.buybuygo.view.seller.fragment.live

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SearchView
import android.view.*
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.data.seller.Channel
import com.techapp.james.buybuygo.presenter.seller.LivePresenter
import kotlinx.android.synthetic.main.seller_fragment_live.*

class LiveFragment : Fragment(), LiveView {
    var streamUrl: String = ""
    var flag = false;
    lateinit var searchView: SearchView
    var descriptionDialog: AlertDialog? = null
    private lateinit var livePresenter: LivePresenter
    lateinit var dialogHelper: DialogHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        dialogHelper = DialogHelper(this)
        livePresenter = LivePresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.seller_fragment_live, container, false)
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        liveWebView.settings.setJavaScriptEnabled(true)
        liveWebView.settings.setJavaScriptCanOpenWindowsAutomatically(false)
        liveWebView.isVerticalScrollBarEnabled = false
        liveWebView.isHorizontalScrollBarEnabled = false
        liveWebView.settings.setAppCacheEnabled(false);
        liveWebView.setWebViewClient(WebViewClient());
        endLiveBtn.setOnClickListener {
            flag = false
            livePresenter.endChannel()
        }
        tokenBtn.setOnClickListener {

            ChannelData.channel?.let {
                dialogHelper.createTokenDialog(this.activity!!, it.channelToken).show()
            }
        }

        if (flag) {
            liveWebView.loadData(streamUrl, "text/html", null)
            updateCommodity()
        }

//        var string = "<!DOCTYPE html>\n" +
//                "    <html>\n" +
//                "    \n" +
//                "        <head>\n" +
//                "            <meta charset=\"UTF-8\">\n" +
//                "            <title></title>\n" +
//                "        </head>\n" +
//                "    \n" +
//                "        <body>\n" +
//                "            <br/>\n" +
//                "            <!--<a href=\"[scheme]://[host]/[path]?[query]\">启动应用程序</a>-->\n" +
//                "            <a href=\"buybuygo://buyer/order\">打开app</a><br/>\n" +
//                "    \n" +
//                "    </html>"
//
//        liveWebView.loadData(string, "text/html", null)
    }

    private fun loadWebView(fbLiveUrl: String) {
        streamUrl = "<html><body>" +
                "<iframe" + " src=\"$fbLiveUrl\"" +
                " width=\"100%\"" +
                " height=\"${root.height}\"" +
                " style=\"border:0;overflow:hidden\"top:0px; left:0px; bottom:0px; right:0px; margin:0; padding=0; " +
                " scrolling=\"no\"" +
                " frameBorder=\"0\"" +
                " allowTransparency=\"true\"" +
                " allowFullScreen=\"true\">" +
                "</iframe></ body></html >"
        liveWebView.loadData(streamUrl, "text/html", null)
        updateCommodity()
    }

    fun updateCommodity() {
        livePresenter.trackLiveTimerSoldItem()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.common_live, menu)
        var searchItem = menu!!.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Give Me a FB Live Url"
        //not show search field
        searchView.setIconifiedByDefault(true)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(input: String?): Boolean {
                    input?.let {
                        //input is a liveUrl which seller live in facebook
                        //post api/Channel to get Channel
                        var url = getFBLiveUrl(input)
                        if (url.equals("Not Thing")) {
                            return true
                        }
                        descriptionDialog = dialogHelper.createDialog()
                        descriptionDialog?.show()
                        descriptionDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
                            ?.setOnClickListener {
                                var descriptonField =
                                    descriptionDialog?.findViewById<EditText>(R.id.descriptionField)
                                var description = descriptonField!!.text.toString()
                                if (!description.equals("")) {
                                    flag = true
                                    livePresenter.startChannel(
                                        url,
                                        description
                                    )
                                }
                            }
                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            })
    }

    fun getFBLiveUrl(fbStreamUrl: String): String {
        var pattern = "^[0-9]*\$".toRegex()
        var sArray = fbStreamUrl.split("/")
        if (sArray.size < 2) {
            return "Not Thing"
        }
//        Timber.d("Video orderNumber ${sArray[sArray.size - 2]}")
        var id = sArray[sArray.size - 2]
        if (!pattern.matches(id)) {
            sArray = fbStreamUrl.split("=")
            id = sArray[sArray.size - 1]
        }
        return "https://www.facebook.com/video/embed?video_id=$id\""
    }

    override fun stopLive() {
        liveWebView.loadUrl("about:blank")
    }

    override fun getChannel(url: String, channel: Channel) {
        ChannelData.channel = channel
        loadWebView(url)
        searchView?.setQuery("", true)
        searchView?.clearFocus()
        searchView?.isIconified = true
        descriptionDialog?.cancel()

    }

    override fun updateCommodity(c: Commodity) {
        this@LiveFragment.activity?.runOnUiThread {
            soldLabel.text = String.format(
                resources.getString(R.string.soldQuantity),
                c.soldQuantity
            )
            remainLabel.text = String.format(
                resources.getString(R.string.remainingQuantity),
                c.remainingQuantity
            )
        }
    }

    override fun showRequestMessage(s: String) {
        Toast.makeText(this.context, s, Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LiveFragment().apply {
                arguments = Bundle().apply {
                    //                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
                }
            }
    }
}
