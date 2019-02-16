package com.techapp.james.buybuygo.model.data.seller

import com.google.gson.annotations.SerializedName

data class PaymentServices(
    var id: String = "",
    var name: String = "",
    @SerializedName("created_at") var createdAt: String = "",
    @SerializedName("updated_at") var updatedAt: String = ""
) {
}