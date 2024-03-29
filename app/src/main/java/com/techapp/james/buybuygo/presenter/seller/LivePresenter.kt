package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.seller.Channel
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.seller.fragment.live.LiveView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LivePresenter {
    var raySeller = RetrofitManager.getInstance().getRaySeller()
    var rayCommon = RetrofitManager.getInstance().getRayCommon()
    var view: LiveView
    var rayToken: String

    constructor(view: LiveView) {
        this.view = view
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun startChannel(liveUrl: String, description: String) {
        var jsonObject = JSONObject()
        jsonObject.put("iFrame", liveUrl)
        jsonObject.put("channel_description", description)
        var body = RequestBody.create(
            MediaType.parse("application/json"),
            jsonObject.toString()
        )
        var singleChannel =
            raySeller.startChannel(rayToken, body)
        singleChannel.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Timber.d(it.isSuccessful.toString() + " channel data ")
                Timber.d("message ${it.message()} + ${it.errorBody()?.string()}")
                it.errorBody()?.let {
                    view.showRequestMessage(it.string())
                }
                if (it.isSuccessful) {
                    it.body()?.let {
                        view.getChannel(liveUrl, it.response)
                    }

                }
            }.doOnError {
                Timber.d("message" + it.message)
            }.subscribe()
    }

    var timerDisposable: Disposable? = null
    fun trackLiveTimerSoldItem() {
        timerDisposable?.dispose()
        var timer =
            Observable.interval(5, TimeUnit.SECONDS)
        timerDisposable = timer.doOnNext {
            var singleLiveSoldItem = rayCommon.getLiveTimerSoldItem(rayToken)
            var response = singleLiveSoldItem.execute()
            var commodity = response.body()?.response
            Timber.d(123.toString() + " " + commodity?.id)
            commodity?.let {
                view.updateCommodity(commodity)
            }
        }.subscribe()
    }


    fun endChannel() {
        timerDisposable?.dispose()
        timerDisposable = null
        view.stopLive()
        var singleEnd = raySeller.endChannel(rayToken)
        singleEnd.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                it.body()?.let {
                    view.showRequestMessage(it.response)

                }
            }.subscribe()
    }
}