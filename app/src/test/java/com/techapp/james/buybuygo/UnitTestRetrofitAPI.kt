package com.techapp.james.buybuygo

import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.junit.Test

import org.junit.Assert.*
import timber.log.Timber

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTestRetrofitAPI {
    var fbToken: String =
        "EAAEpKfFACZA8BAF9HQQ2XBMxVvzsqtVdJY5ZA3J3Jk3826Iolg5FWZBXHegBPQH9v71T0nC9wgeQtpvVAMLu9skOv1QYyWEFeKtYoPBKcLicxzamMuDZC9oZCiUIzV6YWfWz90jkyqPMZAnMEE8ZCfSuZC7KCUZBV7VgZA58P1jUblABqqsTZBoGHeCBntsnyNNY62L5nnij0Qch333gUdCbzASzQ0lbFUJxSsZD"
    var rayToken = "Bearer " + fbToken
    @Test
    fun recipient_limit() {
        var rayBuyer = RetrofitManager.getInstance().getRayBuyer()
        var singleRecipeint = rayBuyer.getRecipients(rayToken)
        singleRecipeint.subscribeOn(Schedulers.newThread())
            .doOnSuccess {
                it.body()?.let {
                    Timber.d(" hello ${it.response.size}")
                    assertTrue("Recipient less six ", it.response.size <= 5)
                }
            }.subscribe()
    }

    @Test
    fun recipient_user() {

    }
}
