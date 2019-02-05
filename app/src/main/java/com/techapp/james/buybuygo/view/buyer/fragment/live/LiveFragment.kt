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
import com.techapp.james.buybuygo.model.converter.GsonConverter
import com.techapp.james.buybuygo.model.data.buyer.PlaceOrder
import com.techapp.james.buybuygo.presenter.buyer.LivePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.buyer_fragment_live.*
import timber.log.Timber


class LiveFragment : Fragment(), com.techapp.james.buybuygo.view.View {
    lateinit var livePresenter: LivePresenter
    lateinit var dialogHelper: DialogHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
            var singleLeave = livePresenter.leaveChannel()
            singleLeave.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    fbLiveWebView.loadUrl("about:blank")
                    progressBar.visibility = View.VISIBLE
                }
                .doOnSuccess {
                    progressBar.visibility = View.GONE
                    it.body()?.let {
                        Toast.makeText(this@LiveFragment.context, it.response, Toast.LENGTH_LONG)
                            .show()
                    }
                    it.errorBody()?.let {
                        var wrapperString = GsonConverter.convertJsonToWrapperString(it.string())
                        Toast.makeText(
                            this@LiveFragment.context,
                            wrapperString.response,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }.subscribe()
        }
        buyBtn.setOnClickListener {
            var singleSoldItem = livePresenter.getLiveSoldItem()
            singleSoldItem.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showLoad()
                }.doOnSuccess {
                    loadDialog?.cancel()
                    if (it.body() == null) {
                        it.errorBody()?.let {
                            var wrapperString = GsonConverter
                                .convertJsonToWrapperString(it.string())
                            Toast.makeText(
                                this@LiveFragment.context,
                                wrapperString.response,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        it.body()?.let {
                            var commodity = it.response
                            var placeOrderDialog = dialogHelper.createPlaceOrderDialog(commodity,
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
                                            var singleString = livePresenter.placeOrder(orderItem)
                                            singleString.subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .doOnSubscribe {
                                                    showLoad()
                                                }.doOnSuccess {
                                                    dialog.cancel()
                                                    loadDialog?.cancel()
                                                    Toast.makeText(
                                                        this@LiveFragment.context,
                                                        it.body()?.response,
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }.subscribe()
                                        }
                                    }
                                })
                            placeOrderDialog.show()
                        }
                    }
                }.subscribe()
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
                        var singleLiveUrl = livePresenter.getLiveUrl(input)
                        singleLiveUrl.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe {
                                progressBar.visibility = View.VISIBLE
                                fbLiveWebView.visibility = View.INVISIBLE
                            }
                            .doOnSuccess {
                                progressBar.visibility = View.GONE
                                fbLiveWebView.visibility = View.VISIBLE
                                if (it.body() == null) {
                                    Toast.makeText(
                                        this@LiveFragment.context,
                                        it.errorBody()?.string(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    it.body()?.let {
                                        loadWebView(it.response)
                                    }
                                }

                            }.subscribe()
                        //input is a url which seller live in facebook
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
    }

    override fun onDetach() {
        super.onDetach()
    }

    var loadDialog: Dialog? = null
    fun showLoad() {
        loadDialog =
                ProgressDialog.show(
                    this@LiveFragment.context,
                    "Loading",
                    "Waiting...",
                    true
                )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LiveFragment()
    }
}
