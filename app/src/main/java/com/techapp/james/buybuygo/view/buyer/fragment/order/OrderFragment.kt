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
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.buyer_fragment_order.*
import timber.log.Timber

class OrderFragment : Fragment(), com.techapp.james.buybuygo.view.View {
    var listener: OnFragmentInteractionListener? = null
    lateinit var orderAdapter: OrderAdapter
    lateinit var orderPresenter: OrderPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        orderPresenter = OrderPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        var singleOrderDetail = orderPresenter.getAllOrder()
        singleOrderDetail.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {

            }
            .doOnSuccess {

            }

        var orderDetail = OrderDetail(
            "sldjf",
            "sljfl",
            "sljfljse",
            "sdkjf",
            "lsdjfl",
            "sldjfl",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf",
            "sldjf"
        )
        var mockList = ArrayList<OrderDetail>()
        for (i in 0..10) {
            mockList.add(orderDetail)
        }
        orderAdapter = OrderAdapter(this.activity!!, mockList)
        orderList.layoutManager = LinearLayoutManager(this.activity)
        orderList.adapter = orderAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.order) {
            // all order
            0 -> {
                Timber.d("pass 0")
                return true
            }
            //new order
            1 -> {
                Timber.d("pass 1")
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

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrderFragment()
    }
}
