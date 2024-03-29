package com.techapp.james.buybuygo.model.data

import com.google.gson.annotations.SerializedName
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import java.io.Serializable

class User(
    var name: String = "",
    var email: String = "",
    @SerializedName("avatar") var avatarUrl: String = "",
    @SerializedName("user_id") var id: String = "",
    var recipients: ArrayList<Recipient> = ArrayList<Recipient>(),
    var isSeller: Boolean = false
) : Serializable {
}