package com.techapp.james.buybuygo.model.retrofitManager

import io.reactivex.Single
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.seller.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface RaySeller {
    //if this(Headers) annotation include same key-value, it will override other annotation header.
    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("items")
    //this annotation will provide a header for you
    @Multipart
    fun uploadItem(@Header("Authorization") token: String, @PartMap() partMap: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: MultipartBody.Part): Single<Response<ResponseBody>>

    @Multipart
    @POST("items/{item_id}")
    fun updateItem(@Header("Authorization") token: String, @Path("item_id") id: Int, @PartMap() partMap: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: MultipartBody.Part? = null): Single<Response<ResponseBody>>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("items")
    fun getUploadedItem(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<Commodity>>>>

    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("channels")
    fun getChannelRecord(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<ChannelRecord>>>>


    @Headers("X-Requested-With: XMLHttpRequest")
    @GET("seller-orders")
    fun getAllOrder(@Header("Authorization") token: String): Single<Response<Wrapper<ArrayList<OrderDetail>>>>

    @GET("sold-items/{channel_id}")
    fun getCommodityByChannel(@Header("Authorization") token: String, @Path("channel_id") channelID: String): Single<Response<Wrapper<ArrayList<CommodityRecord>>>>

    @GET("seller-orders/{channel_id}")
    fun getOrderByChannel(@Header("Authorization") token: String, @Path("channel_id") channelID: String): Single<Response<Wrapper<ArrayList<OrderDetail>>>>


    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "items", hasBody = true)
    fun deleteItem(@Header("Authorization") token: String, @Body itemIds: RequestBody): Single<Response<Wrapper<String>>>


    @Headers("Content-Type: application/json")
    @POST("channel")
    fun startChannel(@Header("Authorization") token: String, @Body iFrame: RequestBody): Single<Response<Wrapper<Channel>>>


    @Headers("Content-Type: application/json")
    @POST("streaming-items/{item_id}")
    fun pushItem(@Header("Authorization") token: String, @Path("item_id") itemId: String): Single<Response<Wrapper<String>>>

    @Headers("Content-Type: application/json")
    @PUT("users-channel-id")
    fun endChannel(@Header("Authorization") token: String): Single<Response<Wrapper<String>>>
}