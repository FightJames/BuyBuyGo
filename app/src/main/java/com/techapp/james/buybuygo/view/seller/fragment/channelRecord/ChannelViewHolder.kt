package com.techapp.james.buybuygo.view.seller.fragment.channelRecord

import android.support.v7.widget.RecyclerView
import android.view.View
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord
import kotlinx.android.synthetic.main.seller_fragment_channel_record_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChannelViewHolder : RecyclerView.ViewHolder {
    constructor(view: View) : super(view)

    fun setData(channelRecord: ChannelRecord) {
        var format = SimpleDateFormat("EEEEEEEE, dd-MMM-yyyy HH:mm:ss", Locale.UK)
        val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var date: Date
        var cal = Calendar.getInstance()

        itemView.liveUrlLabel.text =
                String.format(itemView.liveUrlLabel.text.toString(), channelRecord.liveUrl)

        date = format.parse(channelRecord.startTime)
        cal.time = date
        cal.add(Calendar.HOUR, 8)
        itemView.startTimeLabel.text =
                String.format(
                    itemView.startTimeLabel.text.toString(),
                    outputFormat.format(cal.time)
                )

        date = format.parse(channelRecord.endTime)
        cal.time = date
        cal.add(Calendar.HOUR, 8)
        itemView.endTimeLabel.text =
                String.format(
                    itemView.endTimeLabel.text.toString(),
                    outputFormat.format(cal.time)
                )

        itemView.descriptionLabel.text =
                String.format(itemView.descriptionLabel.text.toString(), channelRecord.description)
    }
}