package com.techapp.james.buybuygo.model.retrofitManager

import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface RayCommon {

    @Headers("Content-Type:application/json",
            "X-Requested-With:XMLHttpRequest")
    @POST("/api/token")
    fun recordUser(@Header("Authorization") token: String, @Body requestBody: RequestBody): Single<Response<ResponseBody>>
}