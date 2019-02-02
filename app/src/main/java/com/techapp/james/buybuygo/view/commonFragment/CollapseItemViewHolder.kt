package com.techapp.james.buybuygo.view.commonFragment

import android.support.v7.widget.RecyclerView
import android.view.View
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Recipient
import kotlinx.android.synthetic.main.common_user_info_collapse.view.*

class CollapseItemViewHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView) {
    }

    fun enableCollapse(input: Boolean) {
        if (input) {
            var isCollapse = true
            itemView.recipientLabel.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    isCollapse = collapse(isCollapse)
                }
            })
            itemView.limitLabel.setOnClickListener {
                isCollapse = collapse(isCollapse)
            }
        }
    }

    private fun collapse(isCollapse: Boolean): Boolean {
        if (isCollapse) {
            itemView.itemContainer.visibility = View.VISIBLE
            return false
        } else {
            itemView.itemContainer.visibility = View.GONE
            return true
        }
    }

    fun setData(currentCount: String = "0") {
        var res = itemView.resources
        itemView.limitLabel.text =
                String.format(
                    res.getString(R.string.limit),
                    res.getString(R.string.limitCount),
                    currentCount
                )
    }

}