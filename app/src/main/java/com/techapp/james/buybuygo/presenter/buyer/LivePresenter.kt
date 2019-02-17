package com.techapp.james.buybuygo.presenter.buyer

import com.techapp.james.buybuygo.model.converter.GsonConverter
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.data.buyer.PlaceOrder
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RayCommon
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.buyer.fragment.live.LiveView
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
import java.util.concurrent.TimeUnit


class LivePresenter {
    var view: LiveView
    var rayBuyer: RayBuyer
    var rayCommon: RayCommon
    var rayToken: String

    constructor(view: LiveView) {
        this.view = view
        var retrofitManager = RetrofitManager.getInstance()
        rayBuyer = retrofitManager.getRayBuyer()
        rayCommon = retrofitManager.getRayCommon()
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun getLiveUrl(channelToken: String) {
        var jsonObject = JSONObject()
        jsonObject.put("channel_token", channelToken)
        var requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            jsonObject.toString()
        )
        var singleLiveUrl = rayBuyer.joinChannel(rayToken, requestBody)
        singleLiveUrl.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWeb(true)
            }
            .doOnSuccess {
                view.isLoadWeb(false)
                if (it.body() == null) {
                    it.errorBody()?.let {
                        view.showRequestMessage(it.string())
                    }
                } else {
                    it.body()?.let {
                        view.loadWeb(it.response)
                    }
                }
            }.subscribe()
    }

    fun leaveChannel() {
        timerDisposable?.dispose()
        var singleLeave = rayBuyer.leaveChannel(rayToken)
        singleLeave.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWeb(true)
                view.stopWeb()
                var commodity = Commodity()
                view.updateCommodityState(commodity)
            }
            .doOnSuccess {
                view.isLoadWeb(false)
                it.body()?.let {
                    view.showRequestMessage(it.response)
                }
                it.errorBody()?.let {
                    var wrapperString = GsonConverter.convertJsonToWrapperString(it.string())
                    view.showRequestMessage(wrapperString.response)
                }
            }.subscribe()
    }

    fun getLiveSoldItem() {
        var singleSoldItem = rayCommon.getLiveSoldItem(rayToken)
        singleSoldItem.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }.doOnSuccess {
                view.isLoadWholeView(false)
                if (it.body() == null) {
                    it.errorBody()?.let {
                        var wrapperString = GsonConverter
                            .convertJsonToWrapperString(it.string())
                        view.showRequestMessage(wrapperString.response)

                    }
                } else {
                    it.body()?.let {
                        var commodity = it.response
                        view.showPlaceOrderDialog(commodity)
                    }
                }
            }.subscribe()
    }

    var timerDisposable: Disposable? = null
    fun trackSoldItem() {
        timerDisposable?.dispose()
        var timer =
            Observable.interval(5, TimeUnit.SECONDS)
        timerDisposable = timer.doOnNext {
            var updateCall = rayCommon.getLiveTimerSoldItem(rayToken)
            var response = updateCall.execute()
            var commodity = response.body()?.response
//                Timber.d(123.toString() + " " + commodity?.orderNumber)
            if (commodity == null) {
            } else {
                view.updateCommodityState(commodity)
            }
        }.subscribe()
    }


    fun placeOrder(orderItem: PlaceOrder) {

        var jsonObject = JSONObject()
        jsonObject.put("number", orderItem.number)
        var body = RequestBody.create(
            MediaType.parse(
                "application/json"
            ), jsonObject.toString()
        )

        var singleString = rayBuyer.placeOrder(
            rayToken,
            orderItem.itemId,
            orderItem.recipientId,
            body
        )

        singleString.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }.doOnSuccess {
                view.isLoadWholeView(false)
                view.closePlaceOrderDialog()
                it.body()?.let {
                    view.showRequestMessage(it.response)
                }
            }.subscribe()
    }
}