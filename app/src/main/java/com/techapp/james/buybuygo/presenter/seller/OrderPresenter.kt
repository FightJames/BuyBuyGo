package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import retrofit2.Response

class OrderPresenter {
    var view: View
    var raySeller = RetrofitManager.getInstance().getRaySeller()
    private var rayToken = SharePreference.getInstance().getRayToken()

    constructor(view: View) {
        this.view = view
    }

    fun getAllOrder(): Single<Response<Wrapper<ArrayList<OrderDetail>>>> {
        return raySeller.getAllOrder(rayToken)
    }
}