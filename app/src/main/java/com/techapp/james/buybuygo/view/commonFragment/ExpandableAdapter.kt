package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.User
import kotlinx.android.synthetic.main.common_user_info_collapse.view.*
import timber.log.Timber

class ExpandableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var layoutInflater: LayoutInflater
    var context: Context
    var mode = BUYER_MODE
    var data: User
    var dialogHelper: DialogHelper

    companion object {
        val BUYER_MODE = 0
        val SELLER_MODE = 1
        val NOT_COLLAPSE = 0
        val COLLAPSE = 1
    }

    constructor(context: Context, user: User) : this(context, 0, user)
    constructor(context: Context, mode: Int, user: User) {
        this.context = context
        this.layoutInflater = LayoutInflater.from(context)
        this.data = user
        this.mode = mode
        dialogHelper = DialogHelper(context as Activity)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
        when (viewHolder) {
            is ItemViewHolder -> {

            }
            is CollapseItemViewHolder -> {

                var leftPadding = context.getResources().getDimension(R.dimen.addressPaddingLeft)
                var rightPadding = context.getResources().getDimension(R.dimen.addressPaddingRight)
                var topPadding = context.getResources().getDimension(R.dimen.addressPaddingTop)
                var textView2 = TextView(context)
//                var address = data.recipients!!.address
                textView2.setPadding(topPadding.toInt(), leftPadding.toInt(), 0, rightPadding.toInt())
                textView2.text = "Hello James"
                viewHolder.itemView.itemContainer.addView(textView2)

                viewHolder.itemView.setOnClickListener(object : View.OnClickListener {
                    var isCollapse = true
                    override fun onClick(v: View?) {
                        if (isCollapse) {
                            viewHolder.itemView.itemContainer.visibility = View.VISIBLE
                            isCollapse = false
                        } else {
                            viewHolder.itemView.itemContainer.visibility = View.GONE
                            isCollapse = true
                        }
                    }
                })

            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 1) COLLAPSE else NOT_COLLAPSE


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            NOT_COLLAPSE -> {
                var view = layoutInflater.inflate(R.layout.common_user_info_no_collapse, parent, false)
                return ItemViewHolder(view)
            }
            else -> {
                var view = layoutInflater.inflate(R.layout.common_user_info_collapse, parent, false)
                return CollapseItemViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = if (mode == BUYER_MODE) 2 else 1

}