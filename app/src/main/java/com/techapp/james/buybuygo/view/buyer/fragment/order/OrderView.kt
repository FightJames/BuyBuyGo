package com.techapp.james.buybuygo.view.buyer.fragment.order

import com.techapp.james.buybuygo.model.data.buyer.OrderDetail
import com.techapp.james.buybuygo.model.data.buyer.OrderDetailView
import com.techapp.james.buybuygo.model.data.seller.PaymentServices

interface OrderView {
    fun updateOrderList(list: ArrayList<OrderDetailView>)
    fun isLoad(flag: Boolean)
    fun isLoadWholeView(flag: Boolean)
    fun showPaymentServiceDialog(list: ArrayList<PaymentServices>)
    fun intentToPaymentActivity(webContent: String, isPayPal: Boolean)
    fun showRequestMessage(message: String)
}