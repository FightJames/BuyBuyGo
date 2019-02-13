package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AlertDialogLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import kotlinx.android.synthetic.main.buyer_fragment_live.view.*
import kotlinx.android.synthetic.main.buyer_order_detial_dialog.view.*
import kotlinx.android.synthetic.main.buyer_order_fragment_list_item.view.*
import timber.log.Timber
import java.text.DateFormat

class ItemViewHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView) {
    }

    fun setData(orderDetail: OrderDetail) {
        itemView.orderIdLabel.text =
                String.format(itemView.context.getString(R.string.orderId), orderDetail.id)
        if (orderDetail.status == "0") {
            itemView.statusLabel.text = itemView.context.getString(R.string.unPaid)
            //order is effective
            if (orderDetail.effective != "0") {
                itemView.payBtn.visibility = View.VISIBLE
                itemView.payBtn.setOnClickListener {
                    Timber.d("James payBtnClick")
                }
            }
        } else {
            itemView.statusLabel.text = itemView.context.getString(R.string.paid)
            itemView.payBtn.visibility = View.GONE
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
        itemView.setOnClickListener {
            Timber.d("James itemviewClick")
        }
    }

    fun createDetialDialog(orderDetail: OrderDetail): Dialog {
        var resource = itemView.context.resources
        var detialView = LayoutInflater.from(itemView.context)
            .inflate(R.layout.buyer_order_detial_dialog, null)
        detialView.channelIDLabel.text =
                String.format(resource.getString(R.string.channelID), orderDetail.channelId)
        detialView.effectiveLabel.text =
                String.format(resource.getString(R.string.effective), orderDetail.effective)
        detialView.orderTimeLabel.text =
                String.format(resource.getString(R.string.orderTime), orderDetail.time)
        detialView.expiryTimeLabel.text =
                String.format(resource.getString(R.string.expiryTime), orderDetail.expiryTime)
        detialView.toBeDeleteTimeLabel.text =
                String.format(
                    resource.getString(R.string.toBeDeleteTime),
                    orderDetail.orderDeleteTime
                )
        var builder = AlertDialog.Builder(itemView.context)
        builder.setView(detialView)

        return builder.create()
    }
}