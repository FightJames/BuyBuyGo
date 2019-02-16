package com.techapp.james.buybuygo.view.seller.fragment.order

import com.techapp.james.buybuygo.model.data.buyer.OrderDetail

interface OrderView {
    fun isLoad(flag:Boolean)
    fun updateList(list:ArrayList<OrderDetail>)
}