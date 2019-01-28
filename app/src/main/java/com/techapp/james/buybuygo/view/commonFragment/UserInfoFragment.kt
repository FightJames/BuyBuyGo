package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Phone
import com.techapp.james.buybuygo.model.data.Recipients
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.presenter.Configure
import kotlinx.android.synthetic.main.fragment_user_info.*

const val MODE = "Mode"

class UserInfoFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var mode: Int = ExpandableAdapter.BUYER_MODE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getInt(MODE)
        }
    }

    override fun onStart() {
        super.onStart()
        userInfoList.layoutManager = LinearLayoutManager(this.activity)
//        var u = User("sdlfj", "sdlfj", "sdlfj", "sdlfj", arrayListOf(Recipients("sdlfj", "sdlfj"), Recipients("sdlfj", "sdlfj")))
        userInfoList.adapter = ExpandableAdapter(this.activity as Activity, mode, Configure.user)
//        userInfoList.adapter = ExpandableAdapter(this.activity as Activity, u)
        var itemDecoration = DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(this.activity as Context, R.drawable.user_info_divider_shape)!!)

        userInfoList.addItemDecoration(itemDecoration)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(mode: Int) =
                UserInfoFragment().apply {
                    arguments = Bundle().apply {
                        putInt(MODE, mode)
                    }
                }
    }
}
