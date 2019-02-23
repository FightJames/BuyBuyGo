package com.techapp.james.buybuygo.view.buyer.fragment.live

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.data.buyer.PlaceOrder
import com.techapp.james.buybuygo.model.data.seller.Channel
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.presenter.buyer.LivePresenter
import com.techapp.james.buybuygo.view.seller.fragment.live.ChannelData
import kotlinx.android.synthetic.main.buyer_fragment_live.*
import timber.log.Timber


class LiveFragment : Fragment(), LiveView {

    lateinit var livePresenter: LivePresenter
    lateinit var dialogHelper: DialogHelper
    lateinit var loadDialog: ProgressDialog
    var isPlay = false
    var streamUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loadDialog = ProgressDialog(
            this@LiveFragment.activity
        )
        livePresenter = LivePresenter(this)
        dialogHelper = DialogHelper(this.activity!!)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.buyer_fragment_live, container, false)
    }

    override fun isLoadWholeView(flag: Boolean) {
        if (flag) {
            loadDialog.setCancelable(false)
            loadDialog.setMessage("Loading...")
            loadDialog.show()
        } else {
            loadDialog.cancel()
        }
    }

    override fun isLoadWeb(flag: Boolean) {
        if (flag) {
            progressBar.visibility = View.VISIBLE
            fbLiveWebView.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            fbLiveWebView.visibility = View.VISIBLE
        }
    }

    override fun loadWeb(url: String) {
        loadWebView(url)
        streamUrl = url
    }

    override fun showRequestMessage(message: String) {
        Toast.makeText(
            this@LiveFragment.context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun stopLive() {
        this@LiveFragment.activity?.runOnUiThread {
            isPlay = false
            fbLiveWebView.loadUrl("about:blank")
            soldQuantity?.let {
                it.visibility = View.INVISIBLE
                it.text = "Sold"
            }
            remainingQuantity?.let {
                it.visibility = View.INVISIBLE
                it.text = "Remain"
            }
        }
    }

    override fun startLive() {
        isPlay = true
        soldQuantity?.let {
            it.visibility = View.VISIBLE
            it.text = "Sold"
        }
        remainingQuantity?.let {
            it.visibility = View.VISIBLE
            it.text = "Remain"
        }
    }

    var placeOrderDialog: Dialog? = null
    override fun showPlaceOrderDialog(commodity: Commodity) {
        placeOrderDialog = dialogHelper.createPlaceOrderDialog(commodity,
            object : DialogHelper.OnPlaceOrderOkPress {
                override fun onOkPress(
                    orderItem: PlaceOrder,
                    dialog: Dialog
                ) {
                    Timber.d("***place an order")
                    if (!(orderItem.itemId.equals("") ||
                                orderItem.number == 0 ||
                                orderItem.recipientId.equals(""))
                    ) {
                        // place an order
                        livePresenter.placeOrder(orderItem)
                    }
                }
            })
        placeOrderDialog?.show()
    }

    override fun closePlaceOrderDialog() {
        placeOrderDialog?.cancel()
    }

    fun init() {
        fbLiveWebView.settings.setJavaScriptEnabled(true)
        //this function map to JS's function window.open()
        fbLiveWebView.settings.setJavaScriptCanOpenWindowsAutomatically(false)
        fbLiveWebView.isVerticalScrollBarEnabled = false
        fbLiveWebView.isHorizontalScrollBarEnabled = false
        fbLiveWebView.settings.setAppCacheEnabled(false);
        fbLiveWebView.setWebViewClient(WebViewClient());
        swiperefresh.setOnRefreshListener {
            fbLiveWebView.reload()
            swiperefresh.isRefreshing = false
        }
        leaveBtn.setOnClickListener {
            livePresenter.leaveChannel()
        }
        buyBtn.setOnClickListener {
            livePresenter.getLiveSoldItem()
        }
        if (isPlay) {
            loadWebView(streamUrl)
            Timber.d("live url " + streamUrl)
            startLive()
        }

        Configure.userState?.let {
            var channel = Channel("", it.channelToken)
            ChannelData.channel = channel
            Timber.d("live url " + getFBLiveUrl(streamUrl) + " user Status " + it.liveUrl)
            isPlay = true
            startLive()
            loadWebView(it.liveUrl)
        }
        Configure.userState = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.buyer_live, menu)
        var searchItem = menu!!.findItem(R.id.token)
        var searchView: SearchView = searchItem.actionView as SearchView

//set SearchView Icon
        val searchIconView =
            searchView.findViewById(android.support.v7.appcompat.R.id.search_button) as ImageView
        searchIconView.setImageResource(R.drawable.ic_token_key_white_24dp)

        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Give Me a Token"
        searchView.setIconifiedByDefault(true)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(input: String?): Boolean {
                    input?.let {
                        livePresenter.getLiveUrl(input)
                        //input is a liveUrl which seller live in facebook
                        //post api/Channel to get Channel
                        searchView.setQuery("", true)
                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            })
    }

    private fun loadWebView(fbLiveUrl: String) {
        var streamUrl = "<html><body>" +
                "<iframe" + " src=\"${getFBLiveUrl(fbLiveUrl)}\"" +
                " width=\"100%\"" +
                " height=\"100%\"" +
                " style=\"border:0;overflow:hidden\"top:0px; left:0px; bottom:0px; right:0px; margin:0; padding=0; " +
                " scrolling=\"no\"" +
                " frameBorder=\"0\"" +
                " allowTransparency=\"true\"" +
                " allowFullScreen=\"true\">" +
                "</iframe></ body></html >"
        fbLiveWebView.loadData(streamUrl, "text/html", null)
        //update cycle
        livePresenter.trackSoldItem()
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

    override fun updateCommodityState(commodity: Commodity) {
        this.activity?.runOnUiThread {
            soldQuantity?.let {
                it.text = String.format(
                    resources.getString(R.string.soldQuantity),
                    commodity.soldQuantity
                )
            }
            remainingQuantity?.let {
                it.text = String.format(
                    resources.getString(R.string.remainingQuantity),
                    commodity.remainingQuantity
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LiveFragment()
    }
}
