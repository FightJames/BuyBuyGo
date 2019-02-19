package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord
import com.techapp.james.buybuygo.model.data.seller.ChannelRecordViewData
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.seller.fragment.channelRecord.ChannelRecordView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.http.Header
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
                    for (i in channelRecords.indices) {
                        var channelRecordViewData = ChannelRecordViewData(
                            channelRecords[i].userID,
                            channelRecords[i].liveUrl,
                            channelRecords[i].id,
                            format.parse(channelRecords[i].startTime),
                            format.parse(channelRecords[i].endTime),
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