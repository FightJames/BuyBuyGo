package com.techapp.james.buybuygo.model.retrofitManager

import com.techapp.james.buybuygo.presenter.Configure
import io.reactivex.Single
import bolts.Task
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface RaySeller {
        @Headers(
            "Content-Type: multipart/form-data"
            , "X-Requested-With: XMLHttpRequest")
    @POST("/api/items")
    @Multipart
    fun insertItem(@Header("Authorization") token: String, @PartMap() partMap: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: MultipartBody.Part): Call<ResponseBody>


}