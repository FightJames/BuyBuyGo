package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.buyer.OrderDetailView
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.seller.fragment.order.OrderView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class OrderPresenter {
    var view: OrderView
    var raySeller = RetrofitManager.getInstance().getRaySeller()
    private var rayToken = SharePreference.getInstance().getRayToken()
    var compositeDisposable: CompositeDisposable

    constructor(view: OrderView) {
        this.view = view
        compositeDisposable = CompositeDisposable()
    }

    fun getAllOrder() {
        var singleOrder = raySeller.getAllOrder(rayToken)
        singleOrder = singleOrder.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
                it.body()?.let {
                    var list = mappingOrderDetailViewData(it.response)
                    list.sortWith(compareByDescending({ it.time }))
                    view.updateOrderList(list)
                }
            }
        compositeDisposable.add(singleOrder.subscribe())

    }

    fun getAllSoldCommodity() {
        var singleOrder = raySeller.getAllSoldCommodity(rayToken)
        singleOrder = singleOrder.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
                it.body()?.let {
                    view.updateCommodityList(it.response)
                }
            }
        compositeDisposable.add(singleOrder.subscribe())

    }

    fun cancelWholeTask() {
        compositeDisposable.clear()
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
            var des=""
            if(list[i].commodityDes!=null){
                des=list[i].commodityDes!!
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
