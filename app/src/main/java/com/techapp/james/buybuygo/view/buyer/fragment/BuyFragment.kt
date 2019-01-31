package com.techapp.james.buybuygo.view.buyer.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.techapp.james.buybuygo.R
import kotlinx.android.synthetic.main.buyer_fragment_buy.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BuyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var channelId: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            channelId = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.buyer_fragment_buy, container, false)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (commodityFragment is OnFragmentInteractionListener) { listener = commodityFragment
//        } else {
//            throw RuntimeException(commodityFragment.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onStart() {
        super.onStart()
        itemList

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                BuyFragment().apply {
                    //                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
                }
    }
}
