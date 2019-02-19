package com.techapp.james.buybuygo.view.buyer.fragment.live

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.webkit.WebViewClient
import android.widget.Toast

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.data.buyer.PlaceOrder
import com.techapp.james.buybuygo.presenter.buyer.LivePresenter
import kotlinx.android.synthetic.main.buyer_fragment_live.*
import timber.log.Timber


class LiveFragment : Fragment(), LiveView {

    lateinit var livePresenter: LivePresenter
    lateinit var dialogHelper: DialogHelper
    lateinit var loadDialog: ProgressDialog
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
    }

    override fun showRequestMessage(message: String) {
        Toast.makeText(
            this@LiveFragment.context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun stopWeb() {
        fbLiveWebView.loadUrl("about:blank")
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
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.common_live, menu)
        var searchItem = menu!!.findItem(R.id.search)
        var searchView: SearchView = searchItem.actionView as SearchView
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
                "<iframe" + " src=\"$fbLiveUrl\"" +
                " width=\"100%\"" +
                " height=\"${root.height}\"" +
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

    override fun onStop() {
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LiveFragment()
    }
}
