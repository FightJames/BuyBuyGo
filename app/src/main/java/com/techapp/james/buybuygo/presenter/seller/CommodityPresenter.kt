package com.techapp.james.buybuygo.presenter.seller

import android.support.v4.app.Fragment
import com.techapp.james.buybuygo.model.data.Commodity
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.file.FileData
import com.techapp.james.buybuygo.model.retrofitManager.RaySeller
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import io.reactivex.Single
import okhttp3.*
import retrofit2.Response
import timber.log.Timber
import java.io.File


class CommodityPresenter {
    private var fragment: Fragment
    private var raySeller: RaySeller

    constructor(fragment: Fragment) {
        this.fragment = fragment
        raySeller = RetrofitManager.getInstance().getRaySeller()
    }

    fun getUploadItem(): Single<Response<Wrapper<ArrayList<Commodity>>>> {
        return raySeller.getUploadedItem(Configure.RAY_ACESS_TOKEN)
    }

    fun insertItem(commodity: Commodity, fileData: FileData): Single<Response<ResponseBody>> {
        var map = HashMap<String, RequestBody>()
        map.put("name", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.name))
        map.put("description", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.description))
        map.put("stock", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.stock.toString()))
        map.put("cost", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.cost.toString()))
        map.put("unit_price", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.unit_price.toString()))

        var file = File(fileData.path)
        Timber.d("file path presenter " + file.absolutePath)
        val requestFile = RequestBody.create(
                MediaType.get("image/jpg"), file)
        val body = MultipartBody.Part.createFormData("images", "cacheImage", requestFile)
        var insertOb = raySeller.uploadItem(Configure.RAY_ACESS_TOKEN, map, body)
        return insertOb
    }

    fun deleteItem(commodity: Commodity): Single<Response<Wrapper<String>>> {
        return raySeller.deleteItem(Configure.RAY_ACESS_TOKEN, commodity.id)
    }

    fun updateItem(commodity: Commodity, fileData: FileData): Single<Response<ResponseBody>> {
        var map = HashMap<String, RequestBody>()
        map.put("name", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.name))
        map.put("description", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.description))
        map.put("stock", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.stock.toString()))
        map.put("cost", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.cost.toString()))
        map.put("unit_price", RequestBody.create(okhttp3.MultipartBody.FORM, commodity.unit_price.toString()))
        map.put("_method", RequestBody.create(okhttp3.MultipartBody.FORM,"PATCH" ))

        var file = File(fileData.path)
        Timber.d("file path presenter " + file.absolutePath)
        val requestFile = RequestBody.create(
                MediaType.get("image/jpg"), file)
        val body = MultipartBody.Part.createFormData("images", "cacheImage", requestFile)
        var updateOb = raySeller.updateItem(Configure.RAY_ACESS_TOKEN, commodity.id.toInt(), map, body)
        return updateOb
    }

}