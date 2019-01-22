package com.techapp.james.buybuygo.model.data

import java.io.File

class Commodity(var name: String, var description: String = "", stock: Int, cost: Int, unit_price: Int, images: File) {
}