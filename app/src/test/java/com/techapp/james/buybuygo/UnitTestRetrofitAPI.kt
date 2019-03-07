package com.techapp.james.buybuygo

import com.techapp.james.buybuygo.model.converter.GsonConverter
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
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
    var rayToken =
        "Bearer EAAEpKfFACZA8BAJmIodR7rchPpofA0sFNFaW4NONV9d9zLYl0yZCKyNmiAjL05gZBwoBSGTEd9a6s0nJRkzXONQLP6rynqWE14l5ZCjBAIIzdFIDZCPNxcmDkMILpLSV3ZA1qk6M4AwvyZAZCNSvZBL6ZB4T4WJNXtWD8ZD"

    @Test
    fun testRecipientSize() {
        var rayBuyer = RetrofitManager.getInstance().getRayBuyer()
        var singleRecipeint = rayBuyer.getRecipients(rayToken)

        singleRecipeint.subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .doOnSuccess {
                it.body()?.let {
                    Timber.d(" hello ${it.response.size}")
                    assertTrue("Recipient less six ", it.response.size <= 5)
                } ?: run {
                    println("normal body is null")
                }
            }.doOnError {
                println(it.message)
            }.subscribe()
    }

    @Test
    fun testRayTokenResponse() {
        var rayCommon = RetrofitManager.getInstance().getRayCommon()
        // token is exist return string
        var singleToken = rayCommon.recordUser(rayToken)
        singleToken.subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .doOnSuccess {
                it.body()?.let {
                    var result = it.string()
                    var wrapperString = GsonConverter.convertJsonToWrapperString(result)
                    assertEquals(wrapperString.response, "The token is effective")
                } ?: run {
                    println("normal body is null")
                }
            }.subscribe()
        // token is invalid
        singleToken = rayCommon.recordUser("Bearer hello")
        singleToken.subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .doOnSuccess {
                assertEquals(it.code(), 401)
                it.errorBody()?.let {
                    var result = it.string()
                    var wrapperString = GsonConverter.convertJsonToWrapperString(result)
//                    println(wrapperString.response)
                    assertEquals(wrapperString.response, "The token is invalid")
                } ?: run {
                    println("error body is null")
                }
            }.subscribe()
    }

    @Test
    fun testGetUser() {
        var rayCommon = RetrofitManager.getInstance().getRayCommon()
        var singleUser = rayCommon.getUser(rayToken)
        singleUser.subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .doOnSuccess {
                it.body()?.let {
                    var user = it.response
                    assertTrue("User name length >0 ", user.name.length > 0)
                    assertTrue("User email is String ", user.email is String)
                    assertTrue("User avater is String ", user.avatarUrl is String)
                    assertTrue("User user id length >0 ", user.id.length > 0)
                    user.phone?.let {
                        assertTrue("User phone code length >0 ", it.code.length > 0)
                        assertTrue("User phone length >0 ", it.number.length > 0)
                    } ?: run {
                        assertTrue("User phone is null ", user.phone == null)
                    }
                } ?: run {
                    println("normal body is null")
                }
            }.subscribe()
    }

    @Test
    fun testGetStreamItem() {
    }
}
