package com.techapp.james.buybuygo.view.seller.fragment.order

import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.seller.CommodityRecord

interface OrderView {
    fun isLoad(flag: Boolean)
    fun updateOrderList(list: ArrayList<OrderDetail>)
    fun updateCommodityList(list: ArrayList<CommodityRecord>)
}