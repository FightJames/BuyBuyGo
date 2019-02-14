package com.techapp.james.buybuygo.model.data.seller

import com.google.gson.annotations.SerializedName

class ChannelRecord(
    @SerializedName("user_id") var userID: String,
    @SerializedName("iFrame") var liveUrl: String,
    @SerializedName("channel_id") var id: String,
    @SerializedName("started_at") var startTime: String,
    @SerializedName("ended_at") var endTime: String,
    @SerializedName("channel_description") var description: String
) {
}