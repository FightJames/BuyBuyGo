package com.techapp.james.buybuygo.view.seller.fragment.channelRecord

import com.techapp.james.buybuygo.model.data.seller.ChannelRecord

interface ChannelRecordView {
    fun isLoad(flag: Boolean)
    fun updateChannelRecordList(list: ArrayList<ChannelRecord>)
}