package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.seller.fragment.channelRecord.ChannelRecordView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.http.Header

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
                it.body()?.let {
                    view.updateChannelRecordList(it.response)
                }
            }.subscribe()
    }
}