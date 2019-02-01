package com.techapp.james.buybuygo.view.commonFragment

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.common_user_info_collapse.view.*

class CollapseItemViewHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView) {
    }

    fun enableCollapse(input: Boolean) {
        if (input) {
            itemView.recipientLabel.setOnClickListener(object : View.OnClickListener {
                var isCollapse = true
                override fun onClick(v: View?) {
                    if (isCollapse) {
                        itemView.itemContainer.visibility = View.VISIBLE
                        isCollapse = false
                    } else {
                        itemView.itemContainer.visibility = View.GONE
                        isCollapse = true
                    }
                }
            })
        } else {
            itemView.setOnClickListener {
                false
            }
        }
    }
}