package com.techapp.james.buybuygo.view.seller.fragment.order

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.CommodityRecord

class CommodityRecordAdapter(var dataList: ArrayList<CommodityRecord>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.seller_activity_channel_order_detail_commodity_record_list_item,
                parent,
                false
            )
        return CommodityRecordItemViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        (viewHolder as CommodityRecordItemViewHolder).setData(dataList[position])
    }
}