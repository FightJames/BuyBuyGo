package com.techapp.james.buybuygo.view.seller.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.webkit.WebViewClient

import com.techapp.james.buybuygo.R
import kotlinx.android.synthetic.main.seller_fragment_live.*
import timber.log.Timber

class LiveFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.seller_fragment_live, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {
        super.onStart()
        liveWebView.settings.setJavaScriptEnabled(true)
        liveWebView.settings.setJavaScriptCanOpenWindowsAutomatically(false)
        liveWebView.isVerticalScrollBarEnabled = false
        liveWebView.isHorizontalScrollBarEnabled = false
        liveWebView.settings.setAppCacheEnabled(false);
        liveWebView.setWebViewClient(WebViewClient());
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
        var streamTestUrl = "<html><body>" +
                "<iframe" + " src=\"https://www.facebook.com/video/embed?video_id=$id\"" +
                " width=\"100%\"" +
                " height=\"${root.height + 500}\"" +
                " style=\"border:0;overflow:hidden\"top:0px; left:0px; bottom:0px; right:0px; margin:0; padding=0; " +
                " scrolling=\"no\"" +
                " frameBorder=\"0\"" +
                " allowTransparency=\"true\"" +
                " allowFullScreen=\"true\">" +
                "</iframe></ body></html >"
        liveWebView.loadData(streamTestUrl, "text/html", null)
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
                            //post api/channel to get channel
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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
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
