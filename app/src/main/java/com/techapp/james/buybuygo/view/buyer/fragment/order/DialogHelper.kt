package com.techapp.james.buybuygo.view.buyer.fragment.order

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.PaymentServices
import kotlinx.android.synthetic.main.buyer_fragment_order_payment_service_dialog.view.*

class DialogHelper {
    var activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    interface PaymentServiceDialogCallback {
        fun onSelected(paymentServices: PaymentServices): Boolean
    }

    fun createPaymentServiceDialog(
        list: ArrayList<PaymentServices>,
        selectItemCallBack: PaymentServiceDialogCallback
    ): Dialog {
        var view = LayoutInflater.from(activity)
            .inflate(R.layout.buyer_fragment_order_payment_service_dialog, null)
        var adapter = PaymentServiceAdapter(list)
        view.paymentServiceList.adapter = adapter
        view.paymentServiceList.layoutManager = LinearLayoutManager(activity)
        view.paymentServiceList.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
3
        var dialog = AlertDialog.Builder(activity)
            .setView(view)
            .setNegativeButton(R.string.cancel,object :DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    p0?.cancel()
                }
            })
            .create()

        adapter.selectListener = object : PaymentServiceAdapter.SelectItemCallBack {
            override fun onSelect(paymentServices: PaymentServices) {
                if (selectItemCallBack.onSelected(paymentServices)) {
                    dialog.cancel()
                }
            }
        }

        return dialog
    }
}