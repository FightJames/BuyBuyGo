package com.techapp.james.buybuygo.model.data.common

import com.google.gson.annotations.SerializedName

class UserStatus(
    @SerializedName("host") var host: Int,
    @SerializedName("iFrame") var liveUrl: String,
    @SerializedName("channel_description") var channelDescription: String,
    @SerializedName("channel_token") var channelToken: String
) {
}