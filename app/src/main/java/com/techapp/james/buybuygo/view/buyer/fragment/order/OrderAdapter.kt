package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.app.Activity
import android.content.ClipData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail

class OrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var activity: Activity
    var dataList: ArrayList<OrderDetail>

    constructor(activity: Activity, dataList: ArrayList<OrderDetail>) {
        this.activity = activity
        this.dataList = dataList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(activity)
            .inflate(R.layout.buyer_order_fragment_list_item, parent, false)
        return ItemViewHolder(view)
    }


    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as ItemViewHolder
        //if image is null ,you must set image src is default image

    }
}