package com.techapp.james.buybuygo.view.seller.fragment.commodity

import com.techapp.james.buybuygo.model.data.seller.Commodity

interface CommodityView {
    fun isLoad(flag: Boolean)
    fun updateCommodityList(list: ArrayList<Commodity>)
    fun showRequestMessage(s:String)
}