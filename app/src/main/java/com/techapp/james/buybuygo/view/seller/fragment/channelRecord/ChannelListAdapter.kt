package com.techapp.james.buybuygo.view.seller.fragment.channelRecord

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord

class ChannelListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var dataList: ArrayList<ChannelRecord>
    var itemClickCallback: OnItemClick? = null

    interface OnItemClick {
        fun onItemClick(channelRecord: ChannelRecord)
    }

    constructor(data: ArrayList<ChannelRecord>) {
        this.dataList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.seller_fragment_channel_record_item, parent, false)
        return ChannelViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as ChannelViewHolder).setData(dataList[position])
        viewHolder.itemView.setOnClickListener {
            itemClickCallback?.onItemClick(dataList[position])
        }
    }
}