package com.techapp.james.buybuygo.view.seller.fragment.live

import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.data.seller.Channel

interface LiveView {
    fun showRequestMessage(s: String)
    fun stopLive()
    fun updateCommodity(c: Commodity)
    fun getChannel(url:String,channel: Channel)

}