package com.techapp.james.buybuygo.view.seller.fragment.channelRecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord
import com.techapp.james.buybuygo.presenter.seller.ChannelRecordPresenter
import com.techapp.james.buybuygo.view.seller.activity.channelOrderDetail.ChannelOrderDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.seller_fragment_channel_record.*

class ChannelRecordFragment : Fragment(), ChannelRecordView {
    lateinit var listAdapter: ChannelListAdapter
    lateinit var channelRecordPresenter: ChannelRecordPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelRecordPresenter = ChannelRecordPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.seller_fragment_channel_record, container, false)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    fun init() {
        listAdapter = ChannelListAdapter(ArrayList<ChannelRecord>())
        listAdapter.itemClickCallback = object : ChannelListAdapter.OnItemClick {
            override fun onItemClick(channelRecord: ChannelRecord) {
                var i = Intent(
                    this@ChannelRecordFragment.activity,
                    ChannelOrderDetailActivity::class.java
                )
                i.putExtra(CHANNEL_ID, channelRecord.id)
                this@ChannelRecordFragment.activity
                    ?.startActivity(i)
            }
        }
        channelRecordList.addItemDecoration(
            DividerItemDecoration(
                this.activity,
                DividerItemDecoration.VERTICAL
            )
        )
        channelRecordList.layoutManager = LinearLayoutManager(this.activity)
        channelRecordList.adapter = listAdapter
        channelRecordPresenter.getChannelRecord()
    }

    override fun updateChannelRecordList(list: ArrayList<ChannelRecord>) {
        listAdapter.dataList = list
        listAdapter.notifyDataSetChanged()
    }

    override fun isLoad(flag: Boolean) {
        loadDataProgressBar?.let {
            if (flag) {
                loadDataProgressBar.visibility = View.VISIBLE
            } else {
                loadDataProgressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChannelRecordFragment()

        val CHANNEL_ID = "CHANNEL_ID"
    }
}
