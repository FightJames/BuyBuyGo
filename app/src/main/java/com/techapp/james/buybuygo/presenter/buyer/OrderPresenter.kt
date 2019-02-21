package com.techapp.james.buybuygo.presenter.buyer

import com.techapp.james.buybuygo.model.converter.GsonConverter
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.buyer.OrderDetailView
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
import java.text.SimpleDateFormat
import java.util.*


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

                it.errorBody()?.let {
                    view.showRequestMessage(it.string())
                }

                println("success " + it.isSuccessful)
                it.body()?.let {
                    var orderDetialViewList = mappingOrderDetailViewData(it.response)
                    orderDetialViewList.sortWith(compareByDescending({ it.time }))
                    println("success fuck" + orderDetialViewList.size + " size " + it.response.size)
                    Timber.d("buyer " + orderDetialViewList.size + " size " + it.response.size)
                    view.updateOrderList(orderDetialViewList)
                }
            }
        singleOrderDetail.subscribe()
    }

    fun getLatestChannalOrder() {
        var singleLatest = rayBuyer.getLatestChannelOrder(rayToken)
        singleLatest = singleLatest.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { }
            .doOnSuccess {
                it.body()?.let {

                    var orderDetailViewList = mappingOrderDetailViewData(it.response)
                    orderDetailViewList.sortWith(compareByDescending({ it.time }))
                    view.updateOrderList(orderDetailViewList)
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
        jsonObject.put("ClintBackURL", "http://buybuygo.tech.tw")
        var isPayPal = false
        if (paymentServices.id == "2") {
            jsonObject.put("source", "mobile")
            isPayPal = true
        }
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
                    if (isPayPal) {
                        var wrapperString = GsonConverter.convertJsonToWrapperString(paymentContent)
                        view.intentToPaymentActivity(wrapperString.response, isPayPal)
                    } else {
                        view.intentToPaymentActivity(paymentContent, isPayPal)
                    }
                }
                it.errorBody()?.let {
                    Timber.d("Pay Money Response Error" + it.string())
                }
            }.subscribe()
    }

    fun mappingOrderDetailViewData(list: ArrayList<OrderDetail>): ArrayList<OrderDetailView> {
        var viewlist = ArrayList<OrderDetailView>()
        var format = SimpleDateFormat("EEEEEEEE, dd-MMM-yyyy HH:mm:ss", Locale.UK)
        var calex = Calendar.getInstance()
        var calti = Calendar.getInstance()
        var calde = Calendar.getInstance()
        for (i in list.indices) {
            calex.time = format.parse(list[i].expiryTime)
            calex.add(Calendar.HOUR, 8)
            calti.time = format.parse(list[i].time)
            calti.add(Calendar.HOUR, 8)
            calde.time = format.parse(list[i].orderDeleteTime)
            calde.add(Calendar.HOUR, 8)
            var des = ""
            if (list[i].commodityDes != null) {
                des = list[i].commodityDes!!
            }
            var image=""
            if (list[i].commodityDes != null) {
                image =list[i].image !!
            }
            var orderDetailView = OrderDetailView(
                list[i].id,
                list[i].orderNumber,
                list[i].userId,
                list[i].channelId,
                list[i].commodityName,
                des,
                list[i].commodityUnitPrice,
                list[i].quantity,
                list[i].totalAmount,
                list[i].status,
                list[i].effective,
                calex.time,
                calti.time,
                image,
                list[i].recipientName,
                list[i].phoneCode,
                list[i].phoneNumber,
                list[i].postCode,
                list[i].country,
                list[i].city,
                list[i].district,
                list[i].addressOthers,
                calde.time
            )
            viewlist.add(orderDetailView)
        }
        return viewlist;
    }

}