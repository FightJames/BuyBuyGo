package com.techapp.james.buybuygo.model.data.common

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token") var token: String = "",
    @SerializedName("expires_in") var exp: String = ""
) {
}