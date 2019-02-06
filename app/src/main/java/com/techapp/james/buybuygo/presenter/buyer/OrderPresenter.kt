package com.techapp.james.buybuygo.presenter.buyer

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import retrofit2.Response

class OrderPresenter {
    var view: View
    var rayBuyer: RayBuyer

    constructor(view: View) {
        this.view = view
        rayBuyer = RetrofitManager.getInstance().getRayBuyer()
    }

    fun getAllOrder(): Single<Response<Wrapper<ArrayList<OrderDetail>>>> {
        return rayBuyer.getAllOrder(Configure.RAY_ACCESS_TOKEN)
    }
}