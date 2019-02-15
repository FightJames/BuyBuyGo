package com.techapp.james.buybuygo.view.seller.fragment.order

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.R.id.action_all_order
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.presenter.seller.OrderPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_buyer.*
import kotlinx.android.synthetic.main.seller_fragment_order.*
import timber.log.Timber

class OrderFragment : Fragment(), com.techapp.james.buybuygo.view.View {
    lateinit var orderPresenter: OrderPresenter
    lateinit var orderAdapter: OrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        orderPresenter = OrderPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.seller_fragment_order, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.seller_order_record, menu)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    fun init() {
        orderAdapter = OrderAdapter(ArrayList<OrderDetail>())
        orderList.layoutManager = LinearLayoutManager(this.activity)
        orderList.adapter = orderAdapter
        updateOrderList()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.order) {
            0 -> {
                updateOrderList()
            }
        }
        return true
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.d("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun updateOrderList() {
        var singleOrder = orderPresenter.getAllOrder()
        singleOrder.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                loadProgressBar.visibility = View.VISIBLE
            }
            .doOnSuccess {
                loadProgressBar.visibility = View.INVISIBLE
                it.body()?.let {
                    orderAdapter.dataList = it.response
                    orderAdapter.notifyDataSetChanged()
                }

            }.subscribe()
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
            OrderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
