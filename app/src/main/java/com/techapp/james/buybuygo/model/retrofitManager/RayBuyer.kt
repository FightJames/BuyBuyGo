package com.techapp.james.buybuygo.model.retrofitManager

import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.seller.PaymentServices
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
    @HTTP(method = "DELETE", path = "recipients", hasBody = true)
    fun deleteRecipient(@Header("Authorization") token: String, @Body itemIds: RequestBody): Single<Response<Wrapper<String>>>

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
    @GET("orders")
    fun getAllOrder(@Header("Authorization") token: String):
            Single<Response<Wrapper<ArrayList<OrderDetail>>>>


    @Headers(
        "Content-Type: application/json",
        "X-Requested-With: XMLHttpRequest"
    )
    @GET("latest-channel-orders")
    fun getLatestChannelOrder(@Header("Authorization") token: String):
            Single<Response<Wrapper<ArrayList<OrderDetail>>>>

    @PUT("user-channel-orderNumber")
    fun leaveChannel(@Header("Authorization") token: String): Single<Response<Wrapper<String>>>

    @GET("payment-services")
    fun getPaymentServices(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<PaymentServices>>>>

    @POST("payments/{thirdPartyPaymentService_id}")
    fun payMoney(@Header("Authorization") token: String, @Path("thirdPartyPaymentService_id") thirdPartyID: String, @Body body: RequestBody): Single<Response<ResponseBody>>

    @HTTP(method = "PATCH", path = "user-channel-orderNumber", hasBody = true)
    fun joinChannel(@Header("Authorization") token: String, @Body body: RequestBody): Single<Response<Wrapper<String>>> //return a seller's live liveUrl

    @POST("orders/{itemId}/{recipientId}")
    fun placeOrder(
        @Header("Authorization") token: String, @Path("itemId") itemId: String,
        @Path("recipientId") recipientId: String, @Body itemIds: RequestBody
    ): Single<Response<Wrapper<String>>>

    @Headers(
        "Content-Type: application/json",
        "X-Requested-With: XMLHttpRequest"
    )
    @HTTP(method = "PATCH", path = "recipients/{recipient_id}", hasBody = true)
    fun updateRecipient(@Header("Authorization") token: String, @Path("recipient_id") id: String, @Body itemIds: RequestBody): Single<Response<Wrapper<String>>>
}