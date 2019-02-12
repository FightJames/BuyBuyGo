package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import kotlinx.android.synthetic.main.buyer_order_fragment_list_item.view.*
import timber.log.Timber

class ItemViewHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView) {
    }

    fun setData(orderDetail: OrderDetail) {
        itemView.orderIdLabel.text =
                String.format(itemView.context.getString(R.string.orderId), orderDetail.id)
        if (orderDetail.status == "0") {
            itemView.statusLabel.text = itemView.context.getString(R.string.unPaid)
        } else {
            itemView.statusLabel.text = itemView.context.getString(R.string.payed)
        }

        itemView.commodityNameLabel.text =
                String.format(
                    itemView.context.getString(R.string.commodityName),
                    orderDetail.commodityName
                )
        itemView.countLabel.text =
                String.format(
                    itemView.context.getString(R.string.remainingQuantity),
                    orderDetail.quantity
                )

        itemView.allPriceLabel.text =
                String.format(
                    itemView.context.getString(R.string.allPrice),
                    orderDetail.totalAmount
                )

        itemView.unitPriceLabel.text =
                String.format(
                    itemView.context.getString(R.string.unitPrice),
                    orderDetail.commodityUnitPrice
                )
        if (orderDetail.image == "") {
        }
        Glide.with(itemView.context).load(orderDetail.image).into(itemView.imageView)

    }
}