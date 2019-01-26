package com.techapp.james.buybuygo.model.retrofitManager

import android.content.Context
import android.net.Uri
import android.support.v4.content.res.ResourcesCompat
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
import java.net.HttpURLConnection
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import io.reactivex.Single
import java.io.IOException
import io.reactivex.internal.disposables.DisposableHelper.isDisposed
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.disposables.Disposable




class Test {

    fun testRecordUser(context: Context, testT: ((context: Context) -> Unit)) {
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
                    testT.invoke(context)
                }
                .doOnError {
                    Timber.d(it.message)
                }.subscribe()
    }

    fun testUpCommodity(context: Context) {

        var f = saveBitmapToFile(context, Bitmap.CompressFormat.PNG, 100)
        Timber.d(context.packageName)
//        val fileUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.test)
//        fileUri
//        var f = File(fileUri.path)

        val requestFile = RequestBody.create(
                MediaType.get("image/png"), f)
        val body = MultipartBody.Part.createFormData("imageUri", "James", requestFile)

        var c = Commodity("hello", "des", 123, 100, 2, Uri.fromFile(f).toString())

        val name = createPartFromString(c.name)
        val description = createPartFromString(c.description)
        val stock = createPartFromString(c.stock.toString())
        val cost = createPartFromString(c.cost.toString())
        Timber.d(c.unit_price.toString())
        val unit_pirce = createPartFromString(c.unit_price.toString())
        var map = HashMap<String, RequestBody>()

        map.put("name", name)
        map.put("description", description)
        map.put("stock", stock)
        map.put("cost", cost)
        map.put("unit_price", unit_pirce)


        //test json

//        var root = JSONObject()
//        root.put("name", name)
//        root.put("description", description)
//        root.put("stock", stock)
//        root.put("cost", cost)
//        root.put("unit_pirce", unit_pirce)
//        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())


        var raySeller = RetrofitManager.getInstance().getRaySeller()
        Timber.d(Configure.FB_ACESS_TOKEN)
//        var s = raySeller.insertItem("Bearer " + Configure.FB_ACESS_TOKEN, map, body)

        var s = raySeller.insertItem("Bearer " + Configure.FB_ACESS_TOKEN, map, body)

//        Timber.d("*** " + s.request().body()?.contentType()?.type())
        s.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Timber.d(it.message())
                    Timber.d(it.isSuccessful.toString())
                    Timber.d(it.body().toString())
                    it.errorBody()?.let {
                        Timber.d(it.string())
                    }
                    Timber.d(it.headers().toString())
                }
                .doOnError { }
                .subscribe()


//        map.put("name", name)
//        map.put("description", description)
//        map.put("stock", stock)
//        map.put("cost", cost)
//        map.put("unit_pirce", unit_pirce)
//
//        val requestBody: RequestBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("name", name.toString())
//                .addFormDataPart("description", description.toString())
//                .addFormDataPart("stock", stock.toString())
//                .addFormDataPart("cost", cost.toString())
//                .addFormDataPart("unit_pirce", unit_pirce.toString())
//                .addFormDataPart("imageUri", "Ray", requestFile)
//                .build()
//        var ob = raySeller.insertItem("Bearer " + Configure.FB_ACESS_TOKEN, requestBody)
//        ob.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//
//                .doOnSuccess {
//                    Timber.d(it.message())
//                    Timber.d(it.isSuccessful.toString())
//                    Timber.d(it.body().toString())
//                    it.errorBody()?.let {
//                        Timber.d(it.string())
//                    }
//                    Timber.d(it.headers().toString())
//                }
//                .doOnError { }
//                .subscribe()
//
//        var obPart = raySeller.insertItem("Bearer " + Configure.FB_ACESS_TOKEN, c.name, c.description, c.stock, c.cost, c.unit_price)
//
//        obPart.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//
//                .doOnSuccess {
//                    Timber.d(it.message())
//                    Timber.d(it.isSuccessful.toString())
//                    Timber.d(it.body().toString())
//                    it.errorBody()?.let {
//                        Timber.d(it.string())
//                    }
//                    Timber.d(it.headers().toString())
//                }
//                .doOnError { }
////                .subscribe()
    }

    fun testGetItems(context: Context) {
        var raySeller = RetrofitManager.getInstance().getRaySeller()
        Timber.d(Configure.FB_ACESS_TOKEN)
        var sW = raySeller.getUploadedItem("Bearer " + Configure.FB_ACESS_TOKEN)
        sW.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    var wrapper = it.body()
                    wrapper?.let {
                        Timber.d(it.result.toString())
                        Timber.d(it.response.size.toString())
                        var data = it.response
                        for (e: Commodity in data) {
                            Timber.d(e.name)
                            Timber.d(e.imageUri)
                        }
                    }
                }
                .doOnError {
                    Timber.d(it.message)
                }.subscribe()
    }

    protected fun createPartFromString(des: String): RequestBody {
        return RequestBody.create(okhttp3.MultipartBody.FORM, des)
    }


    fun saveBitmapToFile(context: Context,
                         format: Bitmap.CompressFormat, quality: Int): File {

        val bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.test)
        val imageFile = File(context.getCacheDir(), "Jjjj.png")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(imageFile)

            bm.compress(format, quality, fos)

            fos.close()

            return imageFile
        } catch (e: IOException) {
            Timber.e("app " + e.message.toString())
            if (fos != null) {
                try {
                    fos.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

            }
        }
        return imageFile
    }
   private fun testRx(){

   }
}