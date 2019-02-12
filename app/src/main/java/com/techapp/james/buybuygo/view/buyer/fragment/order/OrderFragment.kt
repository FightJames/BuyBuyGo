package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.presenter.buyer.OrderPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.buyer_fragment_order.*
import timber.log.Timber

class OrderFragment : Fragment(), com.techapp.james.buybuygo.view.View {
    lateinit var orderAdapter: OrderAdapter
    lateinit var orderPresenter: OrderPresenter
lateinit var compositeDisposable:CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        compositeDisposable= CompositeDisposable()
        orderPresenter = OrderPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        var singleOrderDetail = orderPresenter.getAllOrder()
        var des = singleOrderDetail.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }
            .doOnSuccess {
                Timber.d(456.toString())
                it.body()?.let {
                    Timber.d(456.toString() + it.response.size.toString())
                    orderAdapter = OrderAdapter(this.activity!!, it.response)
                    orderList.adapter = orderAdapter
                    orderAdapter.notifyDataSetChanged()
                }
            }.subscribe()
compositeDisposable.add(des)
        orderList.layoutManager = LinearLayoutManager(this.activity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.order) {
            // all order
            0 -> {
                compositeDisposable.clear()
                Timber.d("pass 0")
                var singleOrderDetail = orderPresenter.getAllOrder()
                var des= singleOrderDetail.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                    }
                    .doOnSuccess {
                        Timber.d(456.toString())
                        it.body()?.let {
                            Timber.d(456.toString() + it.response.size.toString())
                            orderAdapter.dataList = it.response
                            orderAdapter.notifyDataSetChanged()
                        }
                    }.subscribe()

                compositeDisposable.add(des)
                return true
            }
            //new order
            1 -> {
                compositeDisposable.clear()
                Timber.d("pass 1")
                var singleLatest = orderPresenter.getLatestChannalOrder()
                var des = singleLatest.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { }
                    .doOnSuccess {
                        it.body()?.let {
                            Timber.d(456.toString() + it.response.size.toString())
                            orderAdapter.dataList = it.response
                            orderAdapter.notifyDataSetChanged()
                        }
                    }.subscribe()

                compositeDisposable.add(des)
                return true
            }
            else -> {
                Timber.d("pass else")
                return false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.buyer_order_record, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.buyer_fragment_order, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrderFragment()
    }
}
