package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.support.v7.widget.RecyclerView
import android.view.View
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import kotlinx.android.synthetic.main.buyer_order_fragment_list_item.view.*

class ItemViewHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView) {
    }

    fun setData(orderDetail: OrderDetail) {
        itemView.orderIdLabel.text = orderDetail.id
        itemView.statusLabel.text = orderDetail.status
        itemView.commodityNameLabel.text = orderDetail.commodityName
        itemView.countLabel.text=orderDetail.quantity
        itemView
    }
}