package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.seller.ChannelRecord
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Header

class ChannelRecordPresenter {
    var view: View
    private var rayToken: String
    var raySeller = RetrofitManager.getInstance().getRaySeller()

    constructor(view: View) {
        this.view = view
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun getChannelRecord(): Single<Response<Wrapper<ArrayList<ChannelRecord>>>> {
        return raySeller.getChannelRecord(rayToken)
    }
}