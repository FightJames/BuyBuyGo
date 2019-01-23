package com.techapp.james.buybuygo.model.retrofitManager

import android.content.Context
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Commodity
import com.techapp.james.buybuygo.presenter.Configure
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream

class Test {

    fun testRecordUser() {
        var root = JSONObject()
        root.put("expirationDate", Configure.FB_EXPIRATIONDATE)
        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())
        var rayCommon = RetrofitManager.getInstance().getRayCommon()
        var result = rayCommon.recordUser("Bearer " + Configure.FB_ACESS_TOKEN, requestBody)
        result.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Timber.d(it.message())
                    Timber.d(it.isSuccessful.toString())
                    Timber.d(it.body().toString())
//            Timber.d(r.errorBody()!!.string())
                    Timber.d(it.headers().toString())
                }
                .doOnError {
                    Timber.d(it.message)
                }.subscribe()
    }

    fun testUpCommodity(context: Context) {

        var f = getFile(context)
        val requestFile = RequestBody.create(
                MediaType.get("image/png"), f)
        val body = MultipartBody.Part.createFormData("images", "James", requestFile)

        var c = Commodity("hello", "des", 123, 100, 2, f)

        val name = createPartFromString(c.name)
        val description = createPartFromString(c.description)
        val stock = createPartFromString(c.stock.toString())
        val cost = createPartFromString(c.cost.toString())
        val unit_pirce = createPartFromString(c.unit_price.toString())
        var map = HashMap<String, RequestBody>()
        map.put("name", name)
        map.put("description", description)
        map.put("stock", stock)
        map.put("cost", cost)
        map.put("unit_pirce", unit_pirce)

        var raySeller = RetrofitManager.getInstance().getRaySeller()
        Thread({
            Timber.d(Configure.FB_ACESS_TOKEN)
            var s = raySeller.insertItem("Bearer " + Configure.FB_ACESS_TOKEN, map, body)
            var r = s.execute()

            Timber.d(r.message())
            Timber.d(r.isSuccessful.toString())
            Timber.d(r.body().toString())
            Timber.d(r.errorBody()!!.string())
            Timber.d(r.headers().toString())
        }).start()

    }

    fun createPartFromString(des: String): RequestBody {
        return RequestBody.create(okhttp3.MultipartBody.FORM, des)
    }

    fun getFile(context: Context): File {
        var f = File(context.getCacheDir(), "Jjjj.png")
        f.createNewFile()
        var bis = BufferedInputStream(context.getResources().openRawResource(R.raw.test))
        val buffer = ByteArray(bis.available())
        var fout = FileOutputStream(f)
        fout.write(buffer)
        return f
    }
}