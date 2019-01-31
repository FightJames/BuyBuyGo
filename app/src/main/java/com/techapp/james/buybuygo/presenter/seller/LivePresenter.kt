package com.techapp.james.buybuygo.presenter.seller

import android.support.v4.app.Fragment
import com.techapp.james.buybuygo.model.data.Channel
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response

class LivePresenter {
    var fragment: Fragment
    var raySeller = RetrofitManager.getInstance().getRaySeller()

    constructor(fragment: Fragment) {
        this.fragment = fragment
    }

    fun startChannel(iframe: String): Single<Response<Wrapper<Channel>>> {
        var jsonObject = JSONObject()
        jsonObject.put("iFrame", iframe)
        jsonObject.put("channel_description", "hello")
        var body = RequestBody.create(
            MediaType.parse("application/json"),
            jsonObject.toString()
        )

        return raySeller.startChannel(Configure.RAY_ACESS_TOKEN, body)
    }

    fun endChannel(): Single<Response<Wrapper<String>>> {
        return raySeller.endChannel(Configure.RAY_ACESS_TOKEN)
    }
}