package com.techapp.james.buybuygo.presenter.buyer

import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.data.buyer.OrderItem
import com.techapp.james.buybuygo.model.retrofitManager.RayBuyer
import com.techapp.james.buybuygo.model.retrofitManager.RayCommon
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response


class LivePresenter {
    var view: View
    var rayBuyer: RayBuyer
    var rayCommon: RayCommon

    constructor(view: View) {
        this.view = view
        var retrofitManager = RetrofitManager.getInstance()
        rayBuyer = retrofitManager.getRayBuyer()
        rayCommon = retrofitManager.getRayCommon()
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

    fun getLiveSoldItem(): Single<Response<Wrapper<Commodity>>> {
        return rayCommon.getLiveSoldItem(Configure.RAY_ACCESS_TOKEN)
    }

    fun placeOrder(orderItem: OrderItem):Single<Response<Wrapper<String>>> {
        var jsonObject = JSONObject()
        jsonObject.put("number", orderItem.number)
        var body = RequestBody.create(
            MediaType.parse(
                "application/json"
            ), jsonObject.toString()
        )
        return rayBuyer.placeOrder(
            Configure.RAY_ACCESS_TOKEN,
            orderItem.itemId,
            orderItem.recipientId,
            body
        )
    }
}