package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.seller.PaymentServices
import com.techapp.james.buybuygo.presenter.buyer.OrderPresenter
import com.techapp.james.buybuygo.view.buyer.activity.PaymentActivity
import kotlinx.android.synthetic.main.buyer_fragment_order.*
import timber.log.Timber

class OrderFragment : Fragment(), OrderView {
    lateinit var orderAdapter: OrderAdapter
    lateinit var orderPresenter: OrderPresenter
    lateinit var dialogHelper: DialogHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        progressDialog = ProgressDialog(
            this.activity
        )
        dialogHelper = DialogHelper(this.activity!!)
        orderPresenter = OrderPresenter(this)
    }

    override fun updateOrderList(list: ArrayList<OrderDetail>) {
        orderAdapter.dataList = list
        orderAdapter.notifyDataSetChanged()
    }

    override fun isLoad(flag: Boolean) {
        if (flag) {
            loadProgressBar.visibility = View.VISIBLE
            orderList.visibility = View.INVISIBLE
        } else {
            loadProgressBar.visibility = View.INVISIBLE
            orderList.visibility = View.VISIBLE
        }
    }

    var progressDialog: ProgressDialog? = null
    override fun isLoadWholeView(flag: Boolean) {
        if (flag) {
            Timber.d("Progress show")
            progressDialog?.setMessage("Loading...")
            progressDialog?.show()
        } else {
            Timber.d("Progress cancel")
            progressDialog?.cancel()
        }
    }

    override fun showPaymentServiceDialog(list: ArrayList<PaymentServices>) {
        var dialog = dialogHelper.createPaymentServiceDialog(list,
            object : DialogHelper.PaymentServiceDialogCallback {
                override fun onSelected(paymentServices: PaymentServices): Boolean {
                    Timber.d("Select call")
                    orderPresenter.payMoney(paymentServices)
                    return true
                }
            })
        dialog.show()
    }

    override fun intentToPaymentActivity(webContent: String, isPayPal: Boolean) {
        var i = Intent(this.activity, PaymentActivity::class.java)
        Timber.d("Payment URL order $webContent")
        if (isPayPal) {
            i.putExtra(PaymentActivity.PAYMENT_WAY, PaymentActivity.PAYPAL)
        } else {
            i.putExtra(PaymentActivity.PAYMENT_WAY, PaymentActivity.ALLPAY)
        }
        i.putExtra(PaymentActivity.PAYMENT_WEB_CONTENT, webContent)
        this.activity?.startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        orderAdapter = OrderAdapter(this.activity!!, ArrayList<OrderDetail>())
        orderAdapter.payClickListener = object : ItemViewHolder.PayBtnClickListener {
            override fun onClick(id: String) {
                Timber.d("PayBtn click $id")
                orderPresenter.getPaymentService(id)
            }
        }
        orderList.adapter = orderAdapter
        orderList.layoutManager = LinearLayoutManager(this.activity)
        orderList.addItemDecoration(
            DividerItemDecoration(
                this.activity,
                DividerItemDecoration.VERTICAL
            )
        )
        orderPresenter.getAllOrder()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.order) {
            // all order
            0 -> {
                orderPresenter.cancelWholeTask()
                orderPresenter.getAllOrder()
                return true
            }
            //new order
            1 -> {
                orderPresenter.cancelWholeTask()
                orderPresenter.getLatestChannalOrder()
                return true
            }
            else -> {
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
