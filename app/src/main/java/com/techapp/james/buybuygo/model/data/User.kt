package com.techapp.james.buybuygo.model.data

import com.google.gson.annotations.SerializedName

class User(var name: String,
           var email: String,
           @SerializedName("avatar") var avatarUrl: String,
           @SerializedName("user_id") var userId: String) {
}