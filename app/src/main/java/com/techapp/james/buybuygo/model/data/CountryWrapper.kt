package com.techapp.james.buybuygo.model.data

import com.google.gson.annotations.SerializedName

class CountryWrapper(
    var country: String,
    @SerializedName("country_code") var countryCode: String,
    @SerializedName("phone_code") var phoneCode: String
) {
}