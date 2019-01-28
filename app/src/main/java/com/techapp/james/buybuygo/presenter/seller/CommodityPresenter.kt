package com.techapp.james.buybuygo.presenter.seller

import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import com.techapp.james.buybuygo.model.data.Commodity
import com.techapp.james.buybuygo.model.data.Wrapper
import com.techapp.james.buybuygo.model.file.FileData
import com.techapp.james.buybuygo.model.retrofitManager.RaySeller
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.seller.fragment.commodity.ListAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commodity.*
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
        var file = File(Uri.parse(commodity.imageUri).path)

        val requestFile = RequestBody.create(
                MediaType.get("image/png"), File(fileData.path))
        val body = MultipartBody.Part.createFormData("imageUri", file.name, requestFile)
        var insertOb = raySeller.insertItem(Configure.RAY_ACESS_TOKEN, map, body)
        return insertOb
    }
}