package com.techapp.james.buybuygo.presenter.buyer

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import retrofit2.Response

class OrderPresenter {
    var view: View
    var rayBuyer: RayBuyer
    var rayToken: String

    constructor(view: View) {
        this.view = view
        rayBuyer = RetrofitManager.getInstance().getRayBuyer()
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun getAllOrder(): Single<Response<Wrapper<ArrayList<OrderDetail>>>> {
        return rayBuyer.getAllOrder(rayToken)
    }

    fun getLatestChannalOrder(): Single<Response<Wrapper<ArrayList<OrderDetail>>>> {
        return rayBuyer.getLatestChannelOrder(rayToken)
    }
}