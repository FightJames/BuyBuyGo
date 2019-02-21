package com.techapp.james.buybuygo.view.seller.fragment.order

import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.buyer.OrderDetailView
import com.techapp.james.buybuygo.model.data.buyer.OrderEffective
import com.techapp.james.buybuygo.model.data.buyer.OrderStatus
import kotlinx.android.synthetic.main.buyer_order_detial_dialog.view.*
import kotlinx.android.synthetic.main.buyer_order_fragment_list_item.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class OrderItemViewHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView) {
        itemView.payBtn.visibility = View.GONE
        itemView.statusLabel.visibility = View.GONE
    }

    fun setData(orderDetail: OrderDetailView) {
        itemView.statusLabel.visibility = View.VISIBLE
        if (orderDetail.status == OrderStatus.UNPAID.value) {
            itemView.statusLabel.text = itemView.context.getString(R.string.unPaid)
        }else{
            itemView.statusLabel.text = itemView.context.getString(R.string.paid)
        }
        itemView.orderIdLabel.text =
            String.format(itemView.context.getString(R.string.orderId), orderDetail.orderNumber)
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
            var dialog = createDetialDialog(orderDetail)
            dialog.show()
        }
    }

    fun createDetialDialog(orderDetail: OrderDetailView): Dialog {
        val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var resource = itemView.context.resources
        var detialView = LayoutInflater.from(itemView.context)
            .inflate(R.layout.buyer_order_detial_dialog, null)
        var isEffective: String = ""
        if (orderDetail.effective == OrderEffective.EFFECTIVE.value) {
            isEffective = resource.getString(R.string.effective)
        } else {
            isEffective = resource.getString(R.string.unEffective)
        }
        detialView.effectiveLabel.text =
            String.format(resource.getString(R.string.isEffective), isEffective)

        detialView.orderTimeLabel.text =
            String.format(
                resource.getString(R.string.orderTime),
                outputFormat.format(orderDetail.time)
            )

        detialView.expiryTimeLabel.text =
            String.format(
                resource.getString(R.string.expiryTime),
                outputFormat.format(orderDetail.expiryTime)
            )

        detialView.toBeDeleteTimeLabel.text =
            String.format(
                resource.getString(R.string.toBeDeleteTime),
                outputFormat.format(orderDetail.orderDeleteTime)
            )
        var builder = AlertDialog.Builder(itemView.context)
        builder.setView(detialView)
            .setPositiveButton(R.string.ok, null)
        return builder.create()
    }
}