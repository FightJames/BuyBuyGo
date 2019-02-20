package com.techapp.james.buybuygo.model.data.buyer

import java.util.*

class OrderDetailView(
    var id: String = "",
    var orderNumber: String = "",
    var userId: String = "",
    var channelId: String = "",
    var commodityName: String = "",
    var commodityDes: String = "",
    var commodityUnitPrice: String = "",
    var quantity: String = "",
    var totalAmount: String = "",
    var status: String = "",
    var effective: String = "",
    var expiryTime: Date = Date(),
    var time: Date = Date(), //remember to add 8 hour
    var image: String = "",
    var recipientName: String = "",
    var phoneCode: String = "",
    var phoneNumber: String = "",
    var postCode: String = "",
    var country: String = "",
    var city: String = "",
    var district: String = "",
    var addressOthers: String = "",
    var orderDeleteTime: Date = Date()
) {
}