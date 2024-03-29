package com.techapp.james.buybuygo.model.retrofitManager

import android.content.Context
import android.net.Uri
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.Commodity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.io.IOException
import io.reactivex.functions.BiFunction
import retrofit2.Response


class Test {
    var retrofit = RetrofitManager.getInstance()
    var sharedPreference = SharePreference.getInstance()
    fun testRecordUser(context: Context, testT: ((context: Context) -> Unit)) {
        var root = JSONObject()
        root.put("expirationDate", sharedPreference.getExpDate())
        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())
        var rayCommon = RetrofitManager.getInstance().getRayCommon()
        var result = rayCommon.recordUser(sharedPreference.getRayToken(), requestBody)
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
//        val fileUri = Uri.parse("android.resource://" + commodityFragment.packageName + "/" + R.raw.test)
//        fileUri
//        var f = File(fileUri.path)

        val requestFile = RequestBody.create(
            MediaType.get("image/png"), f
        )
        val body = MultipartBody.Part.createFormData("images", "James", requestFile)

        var c = Commodity(
            "sdkjf",
            "hello",
            "des",
            123,
            100,
            2,
            Uri.fromFile(f).toString()
        )

        val name = createPartFromString(c.name)
        val description = createPartFromString(c.description)
        val stock = createPartFromString(c.stock.toString())
        val cost = createPartFromString(c.cost.toString())
        Timber.d(c.unitPrice.toString())
        val unit_pirce = createPartFromString(c.unitPrice.toString())
        var map = HashMap<String, RequestBody>()

        map.put("name", name)
        map.put("description", description)
        map.put("stock", stock)
        map.put("cost", cost)
        map.put("unitPrice", unit_pirce)


        //test json

//        var root = JSONObject()
//        root.put("name", name)
//        root.put("description", description)
//        root.put("stock", stock)
//        root.put("cost", cost)
//        root.put("unit_pirce", unit_pirce)
//        var requestBody = RequestBody.create(MediaType.parse("application/json"), root.toString())


        var raySeller = RetrofitManager.getInstance().getRaySeller()
        Timber.d(sharedPreference.getRayToken())
//        var s = raySeller.uploadItem("Bearer " + Configure.FB_ACCESS_TOKEN, map, body)

        var s = raySeller.uploadItem(sharedPreference.getRayToken(), map, body)

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
//                .addFormDataPart("imageUrl", "Ray", requestFile)
//                .build()
//        var ob = raySeller.uploadItem("Bearer " + Configure.FB_ACCESS_TOKEN, requestBody)
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
//        var obPart = raySeller.uploadItem("Bearer " + Configure.FB_ACCESS_TOKEN, c.name, c.description, c.stock, c.cost, c.unitPrice)
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
        var raySeller = retrofit.getRaySeller()
        Timber.d(sharedPreference.getRayToken())
        var sW = raySeller.getUploadedItem(sharedPreference.getRayToken())
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
                        Timber.d(e.imageUrl)
                    }
                }
            }
            .doOnError {
                Timber.d(it.message)
            }.subscribe()
    }

    fun testGetUser() {
        var rCommon = retrofit.getRayCommon()
        var rBuyer = retrofit.getRayBuyer()
        Timber.d(sharedPreference.getRayToken())
        var uSingle = rCommon.getUser(sharedPreference.getRayToken())
//        uSingle.
// subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSuccess {
//                    Timber.d("userResult " + it.isSuccessful.toString())
//                    var userWrapper = it.body()!!
//                    var user = userWrapper.response
//                    Timber.d(user.name)
//                }.subscribe()

//                .subscribe()
        uSingle.zipWith(
            rBuyer.getRecipients(sharedPreference.getRayToken()),
            object :
                BiFunction<Response<Wrapper<User>>, Response<Wrapper<ArrayList<Recipient>>>, User> {
                override fun apply(
                    t1: Response<Wrapper<User>>,
                    t2: Response<Wrapper<ArrayList<Recipient>>>
                ): User {
                    var userWrapper = t1.body()!!
                    if (t2.isSuccessful) {
                        var recipientsWrapper = t2.body()!!
                        if (recipientsWrapper.result) {
                            var user = userWrapper.response
                            var recipients = recipientsWrapper.response
                            user.recipients = recipients
                            Timber.d("ok " + user.name + " " + user.email)
                            return user
                        }
                    }
                    userWrapper = t1.body()!!
                    var user = userWrapper.response
                    return user
                }
            }).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun testZipWithRxKotlin() {
        var sOnSub = object : SingleOnSubscribe<String> {
            override fun subscribe(emitter: SingleEmitter<String>) {
                emitter.onSuccess("James")
            }
        }

        var s1 = Single.create(sOnSub)
        var s2 = Single.just("hello")
        s1.zipWith(s2, object : BiFunction<String, String, String> {
            override fun apply(t1: String, t2: String): String {
                return "$t1 $t2"
            }
        })
            .doOnSuccess { Timber.d(it) } // James hello
            .doOnSuccess {
                Timber.d("I'm twice")
            }
            .subscribe()
    }

    protected fun createPartFromString(des: String): RequestBody {
        return RequestBody.create(okhttp3.MultipartBody.FORM, des)
    }


    fun saveBitmapToFile(
        context: Context,
        format: Bitmap.CompressFormat, quality: Int
    ): File {

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

    private fun testRegex() {
        var sArray = "https://www.facebook.com/anastasia918.ha/videos/1012107512284640/".split("/")
        Timber.d("Video orderNumber ${sArray[sArray.size - 2]}")
        var id = sArray[sArray.size - 2]
        var pattern = "^[0-9]*\$".toRegex()
        Timber.d("filter ${pattern.matches(id)}")

        sArray =
                "https://m.facebook.com/story.php?story_fbid=2342076282469919&orderNumber=173022516043744".split(
                    "="
                )

        id = sArray[sArray.size - 1]
        Timber.d("Video orderNumber $id")
        Timber.d("filter ${pattern.matches(id)}")
    }
}