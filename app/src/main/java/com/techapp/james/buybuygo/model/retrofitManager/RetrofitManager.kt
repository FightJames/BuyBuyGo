package com.techapp.james.buybuygo.model.retrofitManager

import android.content.Context
import com.techapp.james.buybuygo.model.data.Commodity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class RetrofitManager {

    private var retrofit: Retrofit

    constructor() {
        //https://facebookoptimizedlivestreamsellingsystem.rayawesomespace.space/api/
        var logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        var httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS); // connect timeout
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        retrofit = Retrofit.Builder()
                .baseUrl("https://facebookoptimizedlivestreamsellingsystem.rayawesomespace.space/api/")
//                .baseUrl("https://c910e548.ngrok.io/api/")
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

    fun getRayCommon() = retrofit.create(RayCommon::class.java)
    fun getRaySeller() = retrofit.create(RaySeller::class.java)
    fun getRayBuyer() = retrofit.create(RayBuyer::class.java)
}
