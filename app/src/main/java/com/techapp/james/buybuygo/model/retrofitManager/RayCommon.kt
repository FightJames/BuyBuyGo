package com.techapp.james.buybuygo.model.retrofitManager

import com.techapp.james.buybuygo.model.data.buyer.CountryWrapper
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RayCommon {

    @Headers(
        "Content-Type:application/json",
        "X-Requested-With:XMLHttpRequest"
    )
    @POST("token")
    fun recordUser(@Header("Authorization") token: String, @Body requestBody: RequestBody): Single<Response<ResponseBody>>


    @GET("country-code")
    fun getCountryWrapper(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<CountryWrapper>>>>

    @Headers(
        "Content-Type:application/json",
        "X-Requested-With:XMLHttpRequest"
    )
    @GET("users")
    fun getUser(@Header("Authorization") token: String): Single<Response<Wrapper<User>>>


    @Headers(
        "Content-Type:application/json",
        "X-Requested-With:XMLHttpRequest"
    )
    @GET("streaming-items")
    fun getLiveSoldItem(@Header("Authorization") token: String): Single<Response<Wrapper<Commodity>>>

    @GET("streaming-items")
    fun getLiveTimerSoldItem(@Header("Authorization") token: String): Call<Wrapper<Commodity>>
}