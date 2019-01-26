package com.techapp.james.buybuygo.view.buyer.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

import com.techapp.james.buybuygo.R
import kotlinx.android.synthetic.main.buyer_fragment_live.*


class LiveFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.seller_live, menu)
        var searchItem = menu!!.findItem(R.id.search)
        var searchView: SearchView = searchItem.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.queryHint="Give Me a Token"
        searchView.setIconifiedByDefault(true)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(input: String?): Boolean {
                        input?.let {
                            //                            Toast.makeText(this@BookActivity, p0, Toast.LENGTH_LONG).show()
                            //input is a url which seller live in facebook
                            //post api/channel to get channel

                            searchView.setQuery("", true)

                        }
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        return true
                    }
                })
    }

    override fun onStart() {
        super.onStart()
        var streamUrl = "315693975739074"
        var name = "9clock.sell"
        var id = "2150916384930883"
        val myHtmlString = "<html><body>" +
                "<iframe src=\"https://www.facebook.com/plugins/video.php?href=https%3A%2F%2Fwww.facebook.com%2F$name%2Fvideos%2F$id%2F&show_text=0&width=100%\" width=\"100%\" height=\"100%\" style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" allowTransparency=\"true\" allowFullScreen=\"true\"></iframe></ body></html >"

        var streamTestUrl = "<html><body>" +
                "<iframe" + " src=\"https://www.facebook.com/video/embed?video_id=$id\"" +
                " width=\"100%\"" +
                " height=\"${root.height + 550}\"" +
                " style=\"border:0;overflow:hidden\"top:0px; left:0px; bottom:0px; right:0px; margin:0; padding=0; " +
                " scrolling=\"no\"" +
                " frameBorder=\"0\"" +
                " allowTransparency=\"true\"" +
                " allowFullScreen=\"true\">" +
                "</iframe></ body></html >"

//                .javaScriptEnabled = true
//        fbLiveWebView.loadData(myHtmlString, "text/html", null)
//        fbLiveWebView.webViewClient = WebViewClient()
        fbLiveWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Toast.makeText(this@LiveFragment.activity!!.applicationContext, "Finish", Toast.LENGTH_LONG).show()
            }
        }
        fbLiveWebView.settings.setJavaScriptEnabled(true)
        fbLiveWebView.settings.setJavaScriptCanOpenWindowsAutomatically(false)
        fbLiveWebView.isVerticalScrollBarEnabled = false
        fbLiveWebView.isHorizontalScrollBarEnabled = false
        fbLiveWebView.settings.setAppCacheEnabled(false);
        fbLiveWebView.setWebViewClient(WebViewClient());
        fbLiveWebView.loadData(streamTestUrl, "text/html", null)
        swiperefresh.setOnRefreshListener {
            fbLiveWebView.reload()
            swiperefresh.isRefreshing = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.buyer_fragment_live, container, false)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LiveFragment.
         */
        @JvmStatic
        fun newInstance() =
                LiveFragment().apply {
                    //                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
                }
    }
}
