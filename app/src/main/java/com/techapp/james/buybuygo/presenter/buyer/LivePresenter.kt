package com.techapp.james.buybuygo.presenter.buyer

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response


class LivePresenter {
    var view: View
    var rayBuyer: RayBuyer

    constructor(view: View) {
        this.view = view
        rayBuyer = RetrofitManager.getInstance().getRayBuyer()
    }

    fun getLiveUrl(channelToken: String): Single<Response<Wrapper<String>>> {
        var jsonObject = JSONObject()
        jsonObject.put("channel_token", channelToken)
        var requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            jsonObject.toString()
        )
        return rayBuyer.joinChannel(Configure.RAY_ACCESS_TOKEN, requestBody)
    }

    fun leaveChannel(): Single<Response<Wrapper<String>>> {
        return rayBuyer.leaveChannel(Configure.RAY_ACCESS_TOKEN)
    }
}