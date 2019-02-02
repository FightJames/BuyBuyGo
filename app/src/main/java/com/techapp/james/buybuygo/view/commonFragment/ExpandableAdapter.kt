package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Recipient
import com.techapp.james.buybuygo.model.data.User
import kotlinx.android.synthetic.main.common_user_info_collapse.view.*
import kotlinx.android.synthetic.main.common_user_info_collapse_recipient_item.view.*

class ExpandableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var layoutInflater: LayoutInflater
    var context: Activity
    var mode = BUYER_MODE
    var data: User
    var itemClick: ItemClick? = null

    companion object {
        val BUYER_MODE = 0
        val SELLER_MODE = 1
        val NOT_COLLAPSE = 0
        val COLLAPSE = 1
    }

    interface ItemClick {
        fun createRecipient()
        fun deleteRecipient(recipient: Recipient)
        fun modifyRecipient(recipient: Recipient)
    }

    constructor(context: Activity, user: User) : this(context, BUYER_MODE, user, null)
    constructor(
        context: Activity,
        mode: Int,
        user: User,
        itemClick: ItemClick? = null
    ) {
        this.context = context
        this.layoutInflater = LayoutInflater.from(context)
        this.data = user
        this.mode = mode
        this.itemClick = itemClick
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
        when (viewHolder) {
            is ItemViewHolder -> {
                viewHolder.setData(data, context)
            }
            is CollapseItemViewHolder -> {

                viewHolder.itemView.addRecipientImageView.setOnClickListener {
                    itemClick?.createRecipient()
                }
                viewHolder.setData()
                data.recipients?.let {
                    viewHolder.setData(it.size.toString())
                }
                viewHolder.itemView.itemContainer.removeAllViews()
                data.recipients?.let {
                    viewHolder.enableCollapse(true)
                    it.forEach { r ->
                        //                        Timber.d("1")
                        var recipientView = layoutInflater.inflate(
                            R.layout.common_user_info_collapse_recipient_item,
                            viewHolder.itemView.itemContainer, false
                        )
                        recipientView.deleteImageView.setOnClickListener {
                            itemClick?.deleteRecipient(r)
                        }
                        recipientView.idLabel.text = r.id
                        recipientView.nameLabel.text = r.name
                        recipientView.nameLabel.setOnClickListener {
                            itemClick?.modifyRecipient(r)
                        }
                        recipientView.idLabel.setOnClickListener {
                            itemClick?.modifyRecipient(r)
                        }
                        viewHolder.itemView.itemContainer.addView(recipientView)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 1) COLLAPSE else NOT_COLLAPSE


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            NOT_COLLAPSE -> {
                var view =
                    layoutInflater.inflate(R.layout.common_user_info_no_collapse, parent, false)
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