package com.techapp.james.buybuygo.view.seller.fragment.order

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail

class OrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var dataList: ArrayList<OrderDetail>

    constructor( dataList: ArrayList<OrderDetail>) {
        this.dataList = dataList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.buyer_order_fragment_list_item, parent, false)
        return OrderItemViewHolder(view)
    }


    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as OrderItemViewHolder
        //if image is null ,you must set image src is default image
        viewHolder.setData(dataList[position])

    }
}