package com.techapp.james.buybuygo.model.data.seller

import com.google.gson.annotations.SerializedName

data class CommodityRecord(
    @SerializedName("item_name") var name: String = "",
    @SerializedName("item_description") var description: String = "",
    @SerializedName("cost") var cost: String = "",
    @SerializedName("unit_price") var unitPrice: String = "",
    @SerializedName("profit") var profit: String = "",
    @SerializedName("total_cost") var totalCost: String = "",
    @SerializedName("quantity") var quantity: String = "",
    @SerializedName("turnover") var turnover: String = ""
) {
}