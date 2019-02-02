package com.techapp.james.buybuygo.model.data.buyer

import com.google.gson.annotations.SerializedName


class Recipient(@SerializedName("recipient_id") var id: String,
                var name: String,
                var phone: Phone = Phone(),
                var address: Address = Address()
)


