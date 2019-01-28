package com.techapp.james.buybuygo.model.retrofitManager

import com.techapp.james.buybuygo.model.data.Recipients
import com.techapp.james.buybuygo.model.data.Wrapper
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface RayBuyer {
    @Headers("Content-Type: application/json",
            "X-Requested-With: XMLHttpRequest")
    @GET("recipients")
    fun getRecipients(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<Recipients>>>>
}