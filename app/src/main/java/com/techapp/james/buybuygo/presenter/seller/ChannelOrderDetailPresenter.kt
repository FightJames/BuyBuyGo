package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.seller.CommodityRecord
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import retrofit2.Response

class ChannelOrderDetailPresenter {
    var view: View
    private var rayToken: String
    var raySeller = RetrofitManager.getInstance().getRaySeller()

    constructor(view: View) {
        this.view = view
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun getOrderByChannelID(channelID: String): Single<Response<Wrapper<String>>> =
        raySeller.getOrderByChannel(rayToken, channelID)


    fun getCommodityByChannelID(channelID: String): Single<Response<Wrapper<ArrayList<CommodityRecord>>>> =
        raySeller.getCommodityByChannel(rayToken, channelID)

}