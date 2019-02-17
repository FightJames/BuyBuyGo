package com.techapp.james.buybuygo.presenter.buyer

import com.techapp.james.buybuygo.model.data.seller.PaymentServices
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.buyer.fragment.order.OrderView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber


class OrderPresenter {
    var view: OrderView
    var rayBuyer: RayBuyer
    var rayToken: String
    var compositeDisposable: CompositeDisposable

    constructor(view: OrderView) {
        this.view = view
        rayBuyer = RetrofitManager.getInstance().getRayBuyer()
        rayToken = SharePreference.getInstance().getRayToken()
        compositeDisposable = CompositeDisposable()
    }

    fun cancelWholeTask() {
        compositeDisposable.clear()
    }

    fun getAllOrder() {
        var singleOrderDetail = rayBuyer.getAllOrder(rayToken)
        singleOrderDetail = singleOrderDetail.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
                it.body()?.let {
                    view.updateOrderList(it.response)
                }
            }
        compositeDisposable.add(singleOrderDetail.subscribe())
    }

    fun getLatestChannalOrder() {
        var singleLatest = rayBuyer.getLatestChannelOrder(rayToken)
        singleLatest = singleLatest.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doOnSuccess {
                it.body()?.let {
                    view.updateOrderList(it.response)
                }
            }
        compositeDisposable.add(singleLatest.subscribe())
    }

    var orderID: String = ""
    fun getPaymentService(id: String) {
        orderID = id
        var singlePaymentServices = rayBuyer.getPaymentServices(rayToken)
        singlePaymentServices = singlePaymentServices
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }
            .doOnSuccess {
                view.isLoadWholeView(false)
                it.body()?.let {
                    view.showPaymentServiceDialog(it.response)
                }
            }
        singlePaymentServices.subscribe()
    }

    fun payMoney(paymentServices: PaymentServices) {
        var jsonObject = JSONObject()
        var orderArray = JSONArray()
        orderArray.put(orderID)
        Timber.d("orderID  " + orderID)
        jsonObject.put("order_id", orderArray)
        jsonObject.put("ClintBackURL", "testBack")
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        var singlePay = rayBuyer.payMoney(rayToken, paymentServices.id, requestBody)
        singlePay.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoadWholeView(true)
            }
            .doOnSuccess {
                view.isLoadWholeView(false)

                Timber.d("Pay Money Message" + it.message())
                it.body()?.let {
                    var paymentContent = it.string()
                    Timber.d("Pay Money Response $paymentContent")
                    view.intentToPaymentActivity(paymentContent)
                }
                it.errorBody()?.let {
                    Timber.d("Pay Money Response Error" + it.string())
                }
            }.subscribe()
    }
}