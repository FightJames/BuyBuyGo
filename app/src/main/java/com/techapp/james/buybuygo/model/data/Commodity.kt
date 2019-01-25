package com.techapp.james.buybuygo.model.data

import com.google.gson.annotations.SerializedName


class Commodity(var name: String, var description: String = "", var stock: Int, var cost: Int, var unit_price: Int,@SerializedName("images") var imageUri: String) {
}