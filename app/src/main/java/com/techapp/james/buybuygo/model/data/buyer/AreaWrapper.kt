package com.techapp.james.buybuygo.model.data.buyer

import com.google.gson.annotations.SerializedName

class AreaWrapper(
    @SerializedName("City") var city: String = "",
    @SerializedName("Area") var area: String = "",
    @SerializedName("ZipCode") var zipCode: String = ""
) {
}