package com.techapp.james.buybuygo.model.data.seller

import com.google.gson.annotations.SerializedName

class Channel(
    @SerializedName("channel_id") var channelId: String,
    @SerializedName("channel_token") var channelToken: String
) {
}