package com.techapp.james.buybuygo.model.retrofitManager

import com.techapp.james.buybuygo.model.data.Recipients
import com.techapp.james.buybuygo.model.data.Wrapper
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RayBuyer {
    @Headers(
        "Content-Type: application/json",
        "X-Requested-With: XMLHttpRequest"
    )
    @GET("recipients")
    fun getRecipients(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<Recipients>>>>

    @Headers(
        "Content-Type: application/json",
        "X-Requested-With: XMLHttpRequest"
    )
    @DELETE("recipients")
    fun deleteRecipients(@Header("Authorization") token: String, @Body body: RequestBody): Single<Response<Wrapper<String>>>
}