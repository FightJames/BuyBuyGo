package com.techapp.james.buybuygo.presenter.seller

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import com.techapp.james.buybuygo.model.data.Commodity
import com.techapp.james.buybuygo.model.retrofitManager.RaySeller
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.seller.fragment.commodity.ListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commodity.*
import timber.log.Timber

class CommodityPresenter {
    private var fragment: Fragment
    private var raySeller: RaySeller

    constructor(fragment: Fragment) {
        this.fragment = fragment
        raySeller = RetrofitManager.getInstance().getRaySeller()
    }

    fun getUploadItem(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        var gW = raySeller.getUploadedItem(Configure.RAY_ACESS_TOKEN)
        gW.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    fragment.loadItemProgressBar.visibility = View.GONE
                    var commodityWrapper = it.body()
                    Timber.d("+++ " + it.body()!!.response.size)
                    if (commodityWrapper!!.result) {
                        var commodityList = commodityWrapper.response
                        commodityList?.let {
                            if (commodityList.size != 0) {
                                adapter.let {
                                    (it as ListAdapter).dList = commodityList
                                    it.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
                .doOnError {
                    Timber.d("error  " + it.message)
                }
                .doOnSubscribe {
                    fragment.loadItemProgressBar.visibility = View.VISIBLE
                }
                .subscribe()
    }

    fun insertItem(commodity: Commodity) {

    }
}