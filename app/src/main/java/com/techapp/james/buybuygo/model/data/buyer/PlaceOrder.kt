package com.techapp.james.buybuygo.model.data.buyer

data class PlaceOrder(
    var itemId: String = "",
    var recipientId: String = "",
    var number: Int = 0
) {
}