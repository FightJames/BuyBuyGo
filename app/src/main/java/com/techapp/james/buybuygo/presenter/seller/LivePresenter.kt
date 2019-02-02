package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.seller.Channel
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

class LivePresenter {
    var raySeller = RetrofitManager.getInstance().getRaySeller()
    var view: View

    constructor(view: View) {
        this.view = view
    }

    fun startChannel(liveUrl: String, description: String): Single<Response<Wrapper<Channel>>> {
        var jsonObject = JSONObject()
        jsonObject.put("iFrame", liveUrl)
        jsonObject.put("channel_description", description)
        var body = RequestBody.create(
            MediaType.parse("application/json"),
            jsonObject.toString()
        )

        return raySeller.startChannel(Configure.RAY_ACCESS_TOKEN, body)
    }

    fun endChannel(): Single<Response<Wrapper<String>>> {
        return raySeller.endChannel(Configure.RAY_ACCESS_TOKEN)
    }
}