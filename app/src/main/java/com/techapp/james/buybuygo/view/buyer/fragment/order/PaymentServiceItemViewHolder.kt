package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.buyer_fragment_order_payment_service_dialog_list_item.view.*

class PaymentServiceItemViewHolder : RecyclerView.ViewHolder {
    constructor(view: View) : super(view)

    fun setData(name: String) {
        itemView.nameLabel.text = name
    }
}