package com.techapp.james.buybuygo.model.data

import java.io.File

class Commodity(var name: String, var description: String = "", var stock: Int, var cost: Int, var unit_price: Int, var images: File) {
}