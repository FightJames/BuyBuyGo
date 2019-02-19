package com.techapp.james.buybuygo.view.seller.fragment.channelRecord

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord
import com.techapp.james.buybuygo.model.data.seller.ChannelRecordViewData
import timber.log.Timber

class ChannelListAdapter : RecyclerView.Adapter<ChannelViewHolder> {
    var dataList: ArrayList<ChannelRecordViewData>
    var itemClickCallback: OnItemClick? = null
    var context: Activity

    interface OnItemClick {
        fun onItemClick(channelRecord: ChannelRecordViewData)
    }

    constructor(data: ArrayList<ChannelRecordViewData>, context: Activity) {
        this.dataList = data
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        var view = LayoutInflater.from(context)
            .inflate(R.layout.seller_fragment_channel_record_item, parent, false)
        return ChannelViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(viewHolder: ChannelViewHolder, position: Int) {
        Timber.d("position " + position + " " + dataList[position].startTime.toString())
        (viewHolder as ChannelViewHolder).setData(dataList[position])
        viewHolder.itemView.setOnClickListener {
            itemClickCallback?.onItemClick(dataList[position])
        }
    }
}