package com.techapp.james.buybuygo.model.data.buyer

import com.google.gson.annotations.SerializedName

class OrderDetail(
    var id: String,
    @SerializedName("order") var orderNumber: String = "",
    @SerializedName("user_id") var userId: String = "",
    @SerializedName("name") var commodityName: String = "",
    @SerializedName("description") var commodityDes: String = "",
    @SerializedName("unit_price") var commodityUnitPrice: String = "",
    @SerializedName("quantity") var quantity: String = "",
    @SerializedName("total_amount") var totalAmount: String = "",
    @SerializedName("channel_id") var channelId: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("effective") var effective: String = "",
    @SerializedName("expiry_time") var expiryTime: String = "",
    @SerializedName("time") var time: String = "", //remember to add 8 hour
    @SerializedName("images") var image: String = "",
    @SerializedName("recipient") var recipientName: String = "",
    @SerializedName("phone_code") var phoneCode: String = "",
    @SerializedName("phone_number") var phoneNumber: String = "",
    @SerializedName("post_code") var postCode: String = "",
    @SerializedName("country") var country: String = "",
    @SerializedName("city") var city: String = "",
    @SerializedName("district") var district: String = "",
    @SerializedName("others") var addressOthers: String = "",
    @SerializedName("to_be_deleted_time") var orderDeleteTime: String = ""
) {
}

enum class OrderStatus(val value: String) {
    PAID("1"), UNPAID("0")
}

enum class OrderEffective(val value: String) {
    EFFECTIVE("1"), INEFFECTIVE("0")
}