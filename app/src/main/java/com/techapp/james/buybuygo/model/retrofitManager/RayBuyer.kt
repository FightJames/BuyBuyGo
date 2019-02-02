package com.techapp.james.buybuygo.model.retrofitManager

import com.techapp.james.buybuygo.model.data.Recipient
import com.techapp.james.buybuygo.model.data.Wrapper
import io.reactivex.Single
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface RayBuyer {

    @Headers(
        "Content-Type:application/json",
        "X-Requested-With:XMLHttpRequest"
    )
    @POST("recipients")
    fun createRecipients(@Header("Authorization") token: String, @Body body: RequestBody): Single<Response<ResponseBody>>

    @Headers(
        "Content-Type: application/json",
        "X-Requested-With: XMLHttpRequest"
    )
    @GET("recipients")
    fun getRecipients(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<Recipient>>>>

    @Headers(
        "Content-Type: application/json",
        "X-Requested-With: XMLHttpRequest"
    )
    @HTTP(method = "DELETE", path = "recipients", hasBody = true)
    fun deleteRecipient(@Header("Authorization") token: String, @Body itemIds: RequestBody): Single<Response<Wrapper<String>>>

    @Headers(
        "Content-Type: application/json",
        "X-Requested-With: XMLHttpRequest"
    )
    @HTTP(method = "PATCH", path = "recipients/{recipient_id}", hasBody = true)
    fun updateRecipient(@Header("Authorization") token: String, @Path("recipient_id") id:String, @Body itemIds: RequestBody): Single<Response<Wrapper<String>>>
}