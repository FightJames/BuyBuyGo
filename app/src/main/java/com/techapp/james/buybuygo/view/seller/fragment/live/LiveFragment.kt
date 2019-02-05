package com.techapp.james.buybuygo.view.seller.fragment.live

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SearchView
import android.view.*
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.presenter.seller.LivePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.seller_fragment_live.*
import timber.log.Timber

class LiveFragment : Fragment(), com.techapp.james.buybuygo.view.View {
    var streamUrl: String = ""
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
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
            var singleEnd = livePresenter.endChannel()
            singleEnd.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    it.body()?.let {
                        Toast.makeText(this.context, it.response, Toast.LENGTH_LONG).show()
                    }
                }.subscribe()
        }
        tokenBtn.setOnClickListener {
            ChannelData.channel?.let {
                dialogHelper.createTokenDialog(this.activity!!, it.channelToken).show()
            }
        }
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
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.common_live, menu)
        var searchItem = menu!!.findItem(R.id.search)
        var searchView: SearchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint = "Give Me a FB Live Url"
        searchView.setIconifiedByDefault(true)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(input: String?): Boolean {
                    input?.let {
                        //input is a url which seller live in facebook
                        //post api/Channel to get Channel
                        var url = getFBLiveUrl(input)
                        if (url.equals("Not Thing")) {
                            return true
                        }
                        var dialog = dialogHelper.createDialog()
                        dialog.show()
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setOnClickListener {
                                var descriptonField =
                                    dialog.findViewById<EditText>(R.id.descriptionField)
                                var description = descriptonField!!.text.toString()
                                if (!description.equals("")) {
                                    var singleChannel =
                                        livePresenter.startChannel(
                                            url,
                                            description
                                        )
                                    singleChannel.subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnSuccess {
                                            Timber.d(it.isSuccessful.toString() + " channel data ")
                                            Timber.d("message ${it.message()} + ${it.errorBody()?.string()}")
                                            if (it.isSuccessful) {
                                                ChannelData.channel = it.body()!!.response
                                                loadWebView(url)
                                                searchView.setQuery("", true)
                                                searchView.clearFocus()
                                                searchView.isIconified = true
                                                dialog.cancel()
                                            }
                                        }.doOnError {
                                            Timber.d("message" + it.message)
                                        }.subscribe()
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
//        Timber.d("Video id ${sArray[sArray.size - 2]}")
        var id = sArray[sArray.size - 2]
        if (!pattern.matches(id)) {
            sArray = fbStreamUrl.split("=")
            id = sArray[sArray.size - 1]
        }
        return "https://www.facebook.com/video/embed?video_id=$id\""
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
