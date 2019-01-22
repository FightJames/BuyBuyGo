package com.techapp.james.buybuygo.model.retrofitManager

import android.content.Context
import com.techapp.james.buybuygo.model.data.Commodity
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {

    private var retrofit: Retrofit

    constructor(context: Context) {
        retrofit = Retrofit.Builder()
                .baseUrl("https://facebookoptimizedlivestreamsellingsystem.rayawesomespace.space/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(object :CallAdapter.Factory(){})
                .build()
    }

    companion object {
        private var instance: RetrofitManager? = null
        fun getInstance(context: Context): RetrofitManager {
            if (instance == null) {
                instance = RetrofitManager(context)
            }
            return instance!!
        }
    }

    fun insertCommodity(commodity: Commodity) = retrofit.create(RaySeller::class.java)
}
