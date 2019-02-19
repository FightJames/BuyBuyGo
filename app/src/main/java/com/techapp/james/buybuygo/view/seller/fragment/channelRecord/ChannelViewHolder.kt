package com.techapp.james.buybuygo.view.seller.fragment.channelRecord

import android.support.v7.widget.RecyclerView
import android.view.View
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord
import com.techapp.james.buybuygo.model.data.seller.ChannelRecordViewData
import kotlinx.android.synthetic.main.seller_fragment_channel_record_item.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ChannelViewHolder : RecyclerView.ViewHolder {
    var item: View

    constructor(view: View) : super(view) {
        item = view
    }

    fun setData(channelRecord: ChannelRecordViewData) {
        val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var cal = Calendar.getInstance()
        Timber.d("record " + channelRecord.startTime)
        item.liveUrlLabel.text =
            String.format(
                itemView.context.resources
                    .getString(R.string.liveUrl), channelRecord.liveUrl
            )
        cal.time = channelRecord.startTime
        cal.add(Calendar.HOUR, 8)
        item.startTimeLabel.text =
            String.format(
                itemView.context.resources
                    .getString(R.string.startTime)
                ,
                outputFormat.format(cal.time)
            )


        cal.time = channelRecord.endTime
        cal.add(Calendar.HOUR, 8)
        item.endTimeLabel.text =
            String.format(
                itemView.context.resources
                    .getString(R.string.endTime)
                ,
                outputFormat.format(cal.time)
            )

        item.descriptionLabel.text =
            String.format(
                itemView.context.resources
                    .getString(R.string.description), channelRecord.description
            )

    }
}