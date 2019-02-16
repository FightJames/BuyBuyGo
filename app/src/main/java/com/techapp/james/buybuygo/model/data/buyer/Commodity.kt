package com.techapp.james.buybuygo.model.data.buyer

import com.google.gson.annotations.SerializedName

data class Commodity(
    @SerializedName("item_id") var id: String="",
    var name: String="",
    var description: String = "",
    @SerializedName("unit_price") var unitPrice: String="",
    @SerializedName("image") var imageUrl: String = "",
    @SerializedName("remaining_quantity") var remainingQuantity: String="",
    @SerializedName("sold_quantity") var soldQuantity: String=""
) {
}