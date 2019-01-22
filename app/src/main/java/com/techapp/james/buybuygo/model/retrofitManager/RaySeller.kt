package com.techapp.james.buybuygo.model.retrofitManager

import com.techapp.james.buybuygo.presenter.Configure
import io.reactivex.Single
import bolts.Task
import okhttp3.MultipartBody
import retrofit2.http.*


interface RaySeller {
    @Headers(
            "Content-Type: multipart/form-data"
            , "X-Requested-With: XMLHttpRequest")
    @POST("/items")
    fun insertItem(@Header("Authorization") token: String, @Field("name") name: String, @Field("description") description: String, @Field("stock") stock: Int, @Field("cost") cost: Int, @Field("unit_price") unit_price: Int, @Part("image") file: MultipartBody.Part)


}