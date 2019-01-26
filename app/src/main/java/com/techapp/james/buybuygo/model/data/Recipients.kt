package com.techapp.james.buybuygo.model.data

import com.google.gson.annotations.SerializedName


class Recipients(@SerializedName("recipient_id") var id: String,
                 var name: String,
                 var phone: Phone,
                 var address: Address
)


