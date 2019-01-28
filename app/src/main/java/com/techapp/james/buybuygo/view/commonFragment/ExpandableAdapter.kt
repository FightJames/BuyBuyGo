package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.User
import kotlinx.android.synthetic.main.common_user_info_collapse.view.*
import kotlinx.android.synthetic.main.common_user_info_collapse_recipient_item.view.*
import kotlinx.android.synthetic.main.user_common_dialog_recipient.view.*

class ExpandableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var layoutInflater: LayoutInflater
    var context: Activity
    var mode = BUYER_MODE
    var data: User
    var dialogHelper: DialogHelper

    companion object {
        val BUYER_MODE = 0
        val SELLER_MODE = 1
        val NOT_COLLAPSE = 0
        val COLLAPSE = 1
    }

    constructor(context: Activity, user: User) : this(context, 0, user)
    constructor(context: Activity, mode: Int, user: User) {
        this.context = context
        this.layoutInflater = LayoutInflater.from(context)
        this.data = user
        this.mode = mode
        dialogHelper = DialogHelper(context)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
        when (viewHolder) {
            is ItemViewHolder -> {
                viewHolder.setData(data, context)
            }
            is CollapseItemViewHolder -> {

                viewHolder.itemView.addRecipientImageView.setOnClickListener {
                    dialogHelper.onCreateRecipientDialog(object:DialogHelper.OnOkPress{
                        override fun onOkPress(view: View) {
                        }
                    }).show()
                }
                data.recipients?.let {
                    viewHolder.enableCollapse(true)
                    viewHolder.itemView.itemContainer.removeAllViews()
                    it.forEach { r ->
                        //                        Timber.d("1")
                        var recipientView = layoutInflater.inflate(
                                R.layout.common_user_info_collapse_recipient_item,
                                viewHolder.itemView.itemContainer, false)
                        var phone = r.phone
                        var address = r.address

                        recipientView.idLabel.text = r.id
                        recipientView.nameLabel.text = r.name

                        recipientView.idLabel.setOnClickListener {
                            dialogHelper.onCreateRecipientDialog(r,
                                    object : DialogHelper.OnOkPress {
                                        override fun onOkPress(view: View) {
                                            var phone = r.phone
                                            var address = r.address
                                            r.name = view.nameField.text
                                                    .toString()
                                            phone.code = view.codeField.text
                                                    .toString()
                                            phone.number = view.numberField
                                                    .text.toString()
                                            address.countryCode = view.countryCodeField.text.toString()
                                            address.postCode = view.postCodeField.text.toString()
                                            address.city = view.cityField.text.toString()
                                            address.district = view.districtField.text.toString()
                                            address.others = view.othersField.text.toString()
                                        }
                                    }).show()
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