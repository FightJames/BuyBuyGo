package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.seller.ChannelRecordViewData
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.seller.fragment.channelRecord.ChannelRecordView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ChannelRecordPresenter {
    var view: ChannelRecordView
    private var rayToken: String
    var raySeller = RetrofitManager.getInstance().getRaySeller()

    constructor(view: ChannelRecordView) {
        this.view = view
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun getChannelRecord() {
        var singleChannelRecord = raySeller.getChannelRecord(rayToken)
        singleChannelRecord.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
                var format = SimpleDateFormat("EEEEEEEE, dd-MMM-yyyy HH:mm:ss", Locale.UK)
                it.body()?.let {
                    var channelRecords = it.response
                    var channelRecordList = ArrayList<ChannelRecordViewData>()
                    var calStart = Calendar.getInstance()
                    var calEnd = Calendar.getInstance()
                    for (i in channelRecords.indices) {
                        calStart.time = format.parse(channelRecords[i].startTime)
                        calStart.add(Calendar.HOUR, 8)
                        calEnd.time = format.parse(channelRecords[i].endTime)
                        calEnd.add(Calendar.HOUR, 8)
                        var channelRecordViewData = ChannelRecordViewData(
                            channelRecords[i].userID,
                            channelRecords[i].liveUrl,
                            channelRecords[i].id,
                            calStart.time,
                            calEnd.time,
                            channelRecords[i].description
                        )
                        channelRecordList.add(channelRecordViewData)
                    }
                    channelRecordList.sortWith(compareBy({ it.startTime }))
                    channelRecordList.forEach {
                        Timber.d(it.startTime.toString())
                    }

                    view.updateChannelRecordList(channelRecordList)
                }
            }.subscribe()
    }
}