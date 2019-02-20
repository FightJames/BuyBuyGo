package com.techapp.james.buybuygo.view.buyer.fragment.live

import com.techapp.james.buybuygo.model.data.buyer.Commodity

interface LiveView {
    fun isLoadWholeView(flag: Boolean)
    fun showRequestMessage(message: String)
    fun showPlaceOrderDialog(commodity: Commodity)
    fun closePlaceOrderDialog()
    fun isLoadWeb(flag: Boolean)
    fun startLive()
    fun stopLive()
    fun loadWeb(url: String)
    fun updateCommodityState(commodity: Commodity)
}