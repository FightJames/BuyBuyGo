package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.PaymentServices
import kotlinx.android.synthetic.main.buyer_fragment_order_payment_service_dialog_list_item.view.*
import timber.log.Timber

class PaymentServiceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var dataList: ArrayList<PaymentServices>
    var selectListener: SelectItemCallBack? = null

    interface SelectItemCallBack {
        fun onSelect(paymentServices: PaymentServices)
    }

    constructor(dataList: ArrayList<PaymentServices>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.buyer_fragment_order_payment_service_dialog_list_item, parent,false)
        return PaymentServiceItemViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as PaymentServiceItemViewHolder
        viewHolder.setData(dataList[position].name)
        viewHolder.itemView.nameLabel.setOnClickListener {
            selectListener?.onSelect(dataList[position])
        }
    }
}