package com.techapp.james.buybuygo.model.data.seller

import com.google.gson.annotations.SerializedName


class Commodity(
    @SerializedName("id") var id: String = "",
    var name: String,
    var description: String = "",
    var stock: Int,
    var cost: Int,
    @SerializedName("unit_price") var unitPrice: Int,
    @SerializedName("images") var imageUrl: String
) {
}