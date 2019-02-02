package com.techapp.james.buybuygo.model.data.buyer

import com.google.gson.annotations.SerializedName

class Address(@SerializedName("country_code") var countryCode: String = "",
              @SerializedName("post_code") var postCode: String = "",
              var city: String = "",
              var district: String = "",
              var others: String = "") {
}
