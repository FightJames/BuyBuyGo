package com.techapp.james.buybuygo.view.seller.fragment.live

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.webkit.WebViewClient
import android.widget.Toast

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.presenter.seller.LivePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.seller_fragment_live.*
import timber.log.Timber

class LiveFragment : Fragment() {
    var streamUrl: String = ""
    private lateinit var livePresenter: LivePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        livePresenter = LivePresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.seller_fragment_live, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()
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
    }

    private fun loadWebView(fbStreamUrl: String) {
        var pattern = "^[0-9]*\$".toRegex()
        var sArray = fbStreamUrl.split("/")
        Timber.d("Video id ${sArray[sArray.size - 2]}")
        var id = sArray[sArray.size - 2]
        if (!pattern.matches(id)) {
            sArray = fbStreamUrl.split("=")
            id = sArray[sArray.size - 1]
        }
        Timber.d("filter ${id.toRegex().matches("^[0-9]*\$")}")
        var url = "https://www.facebook.com/video/embed?video_id=$id\""
        streamUrl = "<html><body>" +
                "<iframe" + " src=\"https://www.facebook.com/video/embed?video_id=$id\"" +
                " width=\"100%\"" +
                " height=\"${root.height}\"" +
                " style=\"border:0;overflow:hidden\"top:0px; left:0px; bottom:0px; right:0px; margin:0; padding=0; " +
                " scrolling=\"no\"" +
                " frameBorder=\"0\"" +
                " allowTransparency=\"true\"" +
                " allowFullScreen=\"true\">" +
                "</iframe></ body></html >"
        var singleChannel = livePresenter.startChannel(url)
        singleChannel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Timber.d(it.isSuccessful.toString() + " channel data ")
                Timber.d(
                    "message ${it.message()} + ${it.errorBody()?.string()}"
                )

                if (it.isSuccessful) {
                    ChannelData.channel = it.body()!!.response
                }
            }.doOnError {}.subscribe()
        liveWebView.loadData(streamUrl, "text/html", null)
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.seller_live, menu)
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
                        loadWebView(input)
                        searchView.setQuery("", true)

                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return true
                }
            })
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
