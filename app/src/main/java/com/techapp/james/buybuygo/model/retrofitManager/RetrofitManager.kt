package com.techapp.james.buybuygo.model.retrofitManager

import android.content.Context
import com.techapp.james.buybuygo.model.data.Commodity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class RetrofitManager {

    private var retrofit: Retrofit

    constructor() {
        //https://facebookoptimizedlivestreamsellingsystem.rayawesomespace.space/api/
        var logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        var httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        retrofit = Retrofit.Builder()
                .baseUrl("https://facebookoptimizedlivestreamsellingsystem.rayawesomespace.space/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
//                .addCallAdapterFactory(object :CallAdapter.Factory(){})
                .build()
    }

    companion object {
        private var instance: RetrofitManager? = null
        fun getInstance(): RetrofitManager {
            if (instance == null) {
                instance = RetrofitManager()
            }
            return instance!!
        }
    }

    fun getRaySeller() = retrofit.create(RaySeller::class.java)
    fun getRayCommon() = retrofit.create(RayCommon::class.java)
}
