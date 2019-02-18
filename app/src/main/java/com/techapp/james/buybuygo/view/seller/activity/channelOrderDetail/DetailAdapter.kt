package com.techapp.james.buybuygo.view.seller.activity.channelOrderDetail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.seller.CommodityRecord
import kotlinx.android.synthetic.main.buyer_order_fragment_list_item.view.*

class DetailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var orderDataList = ArrayList<OrderDetail>()
    var commodityDataList = ArrayList<CommodityRecord>()
    var currentMode = ORDER_MODE

    companion object {
        val ORDER_MODE = 0
        val COMMODITY_MODE = 1
    }

    constructor() {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        var inflater = LayoutInflater.from(parent.context)
        when (currentMode) {
            ORDER_MODE -> {
                view = inflater.inflate(R.layout.buyer_order_fragment_list_item, parent, false)
                view.payBtn.visibility = View.GONE
                return OrderViewHolder(view)
            }
            else -> {
                view = inflater.inflate(
                    R.layout.seller_activity_channel_order_detail_commodity_record_list_item,
                    parent,
                    false
                )
                return CommodityViewHolder(view)
            }
        }
    }


    override fun getItemCount(): Int {
        if (currentMode == ORDER_MODE) {
            return orderDataList.size
        } else {
            return commodityDataList.size
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as OrderViewHolder
        //if image is null ,you must set image src is default image
        viewHolder.setData(orderDataList[position])

    }
}