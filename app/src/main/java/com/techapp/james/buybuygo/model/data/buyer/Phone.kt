package com.techapp.james.buybuygo.model.data.buyer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Phone(@SerializedName("phone_code") var code: String = "",
            @SerializedName("phone_number") var number: String = ""):Serializable
