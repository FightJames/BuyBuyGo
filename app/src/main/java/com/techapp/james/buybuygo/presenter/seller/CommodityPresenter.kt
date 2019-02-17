package com.techapp.james.buybuygo.presenter.seller

import com.techapp.james.buybuygo.model.data.seller.Commodity
import com.techapp.james.buybuygo.model.file.FileData
import com.techapp.james.buybuygo.model.networkManager.NetworkManager
import com.techapp.james.buybuygo.model.retrofitManager.RaySeller
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.view.seller.fragment.commodity.CommodityView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.File


class CommodityPresenter {
    private var raySeller: RaySeller
    var view: CommodityView
    var rayToken: String

    constructor(view: CommodityView) {
        this.view = view
        raySeller = RetrofitManager.getInstance().getRaySeller()
        rayToken = SharePreference.getInstance().getRayToken()
    }

    fun deleteItem(commodity: Commodity) {
        var jsonObject = JSONObject()
        var jsonArray = JSONArray()
        jsonArray.put(commodity.id.toInt())
        jsonObject.put("items", jsonArray)
        var requestBody =
            RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        var singleDelete = raySeller.deleteItem(rayToken, requestBody)
        singleDelete.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                it.body()?.let {
                    view.showRequestMessage(it.response)
                }
                getUploadItem()
            }
            .doOnError {
            }.subscribe()
    }

    fun getUploadItem() {
        var singleUploadedItem = raySeller.getUploadedItem(rayToken)
        singleUploadedItem.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                view.isLoad(false)
                it.body()?.let {
                    Timber.d("+++ " + it.response.size)
                    if (it.result) {
                        //it is commodityWrapper
                        var commodityList = it.response
                        commodityList?.let {
                            view.updateCommodityList(it)
                        }
                    }
                }
            }
            .doOnError {
                Timber.d("error  " + it.message)
            }
            .subscribe()
    }

    fun insertItem(commodity: Commodity, fileData: FileData) {
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
            RequestBody.create(okhttp3.MultipartBody.FORM, commodity.unitPrice.toString())
        )

        var file = File(fileData.path)
        Timber.d("file path presenter " + file.absolutePath)
        val requestFile = RequestBody.create(
            MediaType.get("image/jpg"), file
        )
        val body = MultipartBody.Part.createFormData("images", "cacheImage", requestFile)
        var singleInsert = raySeller.uploadItem(rayToken, map, body)
        singleInsert.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.isLoad(true)
            }
            .doOnSuccess {
                Timber.d("success message " + it.message())
                Timber.d("success message " + it.errorBody()?.string())
                getUploadItem()
            }
            .doOnError {
                Timber.d("error " + it.message)
            }
            .subscribe()
    }


    fun updateItem(commodity: Commodity, fileData: FileData) {
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
            RequestBody.create(okhttp3.MultipartBody.FORM, commodity.unitPrice.toString())
        )
        map.put("_method", RequestBody.create(okhttp3.MultipartBody.FORM, "PATCH"))
        var file = File(fileData.path)
        Timber.d("file path presenter " + file.absolutePath)
        var body: MultipartBody.Part? = null
        if (commodity.isModifyImage) {
            Timber.d("modifyImage")
            val requestFile = RequestBody.create(
                MediaType.get("image/jpg"), file
            )
            body = MultipartBody.Part.createFormData("images", "cacheImage", requestFile)
            var singleUpdate =
                raySeller.updateItem(rayToken, commodity.id.toInt(), map, body)
            singleUpdate.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.isLoad(true)
                }
                .doOnSuccess {
                    getUploadItem()
                }.doOnError {
                }.subscribe()
        } else {
            Timber.d("NomodifyImage")
            var singleUpdate =
                raySeller.updateItem(rayToken, commodity.id.toInt(), map, body)
            singleUpdate.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view.isLoad(true)
                }
                .doOnSuccess {
                    getUploadItem()
                }.doOnError {
                }.subscribe()
        }
    }

    fun pushItem(commodity: Commodity) {
        var singlePush = raySeller.pushItem(rayToken, commodity.id)
        singlePush.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                it.body()?.let {
                    view.showRequestMessage(it.response)
                }
            }.subscribe()
    }

}