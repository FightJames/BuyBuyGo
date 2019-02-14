package com.techapp.james.buybuygo.view.seller.activity.channelOrderDetail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.converter.GsonConverter
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.presenter.seller.ChannelOrderDetailPresenter
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.seller.fragment.channelRecord.ChannelRecordFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.seller_activity_channel_order_detail.*

class ChannelOrderDetailActivity : AppCompatActivity(), View {
    lateinit var channelOrderDetailPresenter: ChannelOrderDetailPresenter
    lateinit var channelID: String
    lateinit var detailAdapter: DetailAdapter
    var compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_activity_channel_order_detail)
        channelOrderDetailPresenter = ChannelOrderDetailPresenter(this)
        channelID = intent.getStringExtra(ChannelRecordFragment.CHANNEL_ID)
        init()
    }

    fun init() {
        detailAdapter = DetailAdapter()
        detailList.layoutManager = LinearLayoutManager(this)
        detailList.adapter = detailAdapter
        getCommodityByOrder()
    }

    fun getCommodityByOrder() {
        compositeDisposable.clear()
        var singleCommodity = channelOrderDetailPresenter.getCommodityByChannelID(channelID)
        var disposable = singleCommodity.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                detailList.visibility = android.view.View.INVISIBLE
                progressBar.visibility = android.view.View.VISIBLE
            }
            .doOnSuccess {
                progressBar.visibility = android.view.View.INVISIBLE
                detailList.visibility = android.view.View.VISIBLE
                it.body()?.let {
                    detailAdapter.commodityDataList = it.response
                    detailAdapter.currentMode = DetailAdapter.COMMODITY_MODE
                    detailAdapter.notifyDataSetChanged()
                }
            }.subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
//        menuInflater
        menuInflater.inflate(R.menu.seller_order_detail, menu)
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.order) {
                0 -> {
                    getCommodityByOrder()
                    return true
                }
                1 -> {
                    compositeDisposable.clear()
                    var singleOrder = channelOrderDetailPresenter.getOrderByChannelID(channelID)
                    var disposable = singleOrder.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            detailList.visibility = android.view.View.INVISIBLE
                            progressBar.visibility = android.view.View.VISIBLE
                        }
                        .doOnSuccess {
                            progressBar.visibility = android.view.View.INVISIBLE
                            detailList.visibility = android.view.View.VISIBLE
                            if (it.code() == 200) {
                                it.body()?.let {
                                    if (it.response.length > 3) {
                                        var data =
                                            GsonConverter.gson.fromJson<ArrayList<OrderDetail>>(
                                                it.response
                                            )
                                        detailAdapter.orderDataList = data
                                        detailAdapter.currentMode = DetailAdapter.ORDER_MODE
                                        detailAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }.subscribe()
                    compositeDisposable.add(disposable)
                    return true
                }
                else -> {
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inline fun <reified T> Gson.fromJson(json: String) =
        this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}
