package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.seller.Commodity
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.file.FileData
import com.techapp.james.buybuygo.model.retrofitManager.RaySeller
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.View
import io.reactivex.Single
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.File


class CommodityPresenter {
    private var raySeller: RaySeller
    var view: View

    constructor(view: View) {
        this.view = view
        raySeller = RetrofitManager.getInstance().getRaySeller()
    }

    fun deleteItem(commodity: Commodity): Single<Response<Wrapper<String>>> {
        var jsonObject = JSONObject()
        var jsonArray = JSONArray()
        jsonArray.put(commodity.id.toInt())
        jsonObject.put("items", jsonArray)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        return raySeller.deleteItem(Configure.RAY_ACCESS_TOKEN, requestBody)
    }

    fun getUploadItem(): Single<Response<Wrapper<ArrayList<Commodity>>>> {
        return raySeller.getUploadedItem(Configure.RAY_ACCESS_TOKEN)
    }

    fun insertItem(commodity: Commodity, fileData: FileData): Single<Response<ResponseBody>> {
        var map = HashMap<String, RequestBody>()
        map.put("name", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.name))
        map.put(
            "description",
            RequestBody.create(okhttp3.MultipartBody.FORM, commodity.description)
        )
        map.put("stock", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.stock.toString()))
        map.put("cost", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.cost.toString()))
        map.put(
            "unit_price",
            RequestBody.create(okhttp3.MultipartBody.FORM, commodity.unit_price.toString())
        )

        var file = File(fileData.path)
        Timber.d("file path presenter " + file.absolutePath)
        val requestFile = RequestBody.create(
            MediaType.get("image/jpg"), file
        )
        val body = MultipartBody.Part.createFormData("images", "cacheImage", requestFile)
        var insertOb = raySeller.uploadItem(Configure.RAY_ACCESS_TOKEN, map, body)
        return insertOb
    }


    fun updateItem(commodity: Commodity, fileData: FileData): Single<Response<ResponseBody>> {
        var map = HashMap<String, RequestBody>()
        map.put("name", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.name))
        map.put(
            "description",
            RequestBody.create(okhttp3.MultipartBody.FORM, commodity.description)
        )
        map.put("stock", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.stock.toString()))
        map.put("cost", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.cost.toString()))
        map.put(
            "unit_price",
            RequestBody.create(okhttp3.MultipartBody.FORM, commodity.unit_price.toString())
        )
        map.put("_method", RequestBody.create(okhttp3.MultipartBody.FORM, "PATCH"))

        var file = File(fileData.path)
        Timber.d("file path presenter " + file.absolutePath)
        val requestFile = RequestBody.create(
            MediaType.get("image/jpg"), file
        )
        val body = MultipartBody.Part.createFormData("images", "cacheImage", requestFile)
        var updateOb =
            raySeller.updateItem(Configure.RAY_ACCESS_TOKEN, commodity.id.toInt(), map, body)
        return updateOb
    }

    fun pushItem(commodity: Commodity): Single<Response<Wrapper<String>>> {
        return raySeller.pushItem(Configure.RAY_ACCESS_TOKEN, commodity.id)
    }

}