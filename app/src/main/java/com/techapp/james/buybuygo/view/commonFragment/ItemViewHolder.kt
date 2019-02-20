package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.model.data.common.User
import kotlinx.android.synthetic.main.common_user_info_no_collapse.view.*
import timber.log.Timber

class ItemViewHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView) {
    }

    fun setData(data: User, activity: Activity) {
        itemView.nameField.text = data.name
        Timber.d(data.avatarUrl)
//.replace("large","small")
        Glide.with(activity)
                .load(data.avatarUrl)
                .into(itemView.avatarImageView)
        itemView.emailField.text = data.email
    }
}