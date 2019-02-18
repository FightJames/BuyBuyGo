package com.techapp.james.buybuygo.view.seller.fragment.order

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.seller.CommodityRecord
import com.techapp.james.buybuygo.presenter.seller.OrderPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.seller_fragment_order.*
import timber.log.Timber

class OrderFragment : Fragment(), OrderView {

    lateinit var orderPresenter: OrderPresenter
    lateinit var orderAdapter: OrderAdapter
    lateinit var commodityRecordAdapter: CommodityRecordAdapter
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
        commodityRecordAdapter = CommodityRecordAdapter(ArrayList<CommodityRecord>())
        orderList.layoutManager = LinearLayoutManager(this.activity)
        orderList.adapter = orderAdapter
        orderPresenter.getAllOrder()
    }

    override fun isLoad(flag: Boolean) {
        if (flag) {
            loadProgressBar.visibility = View.VISIBLE
        } else {
            loadProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun updateOrderList(list: ArrayList<OrderDetail>) {
        orderList.adapter = orderAdapter
        orderAdapter.dataList = list
        orderAdapter.notifyDataSetChanged()
    }

    override fun updateCommodityList(list: ArrayList<CommodityRecord>) {
        orderList.adapter = commodityRecordAdapter
        commodityRecordAdapter.dataList = list
        commodityRecordAdapter.notifyDataSetChanged()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.order) {
            0 -> {
                orderPresenter.getAllOrder()
            }
            1 -> {
                orderPresenter.getAllSoldCommodity()
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        orderPresenter.cancelWholeTask()
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
