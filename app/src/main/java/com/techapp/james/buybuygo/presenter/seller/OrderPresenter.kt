package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.seller.fragment.order.OrderView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

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
                    view.updateList(it.response)
                }
            }
        compositeDisposable.add(singleOrder.subscribe())

    }

    fun cancelWholeTask() {
        compositeDisposable.clear()
    }
}
