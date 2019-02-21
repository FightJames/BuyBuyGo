package com.techapp.james.buybuygo.view.buyer.fragment.live

import android.app.Activity
import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.Commodity
import com.techapp.james.buybuygo.model.data.buyer.PlaceOrder
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.presenter.Configure
import kotlinx.android.synthetic.main.buyer_live_commodity_dialog.view.*
import kotlinx.android.synthetic.main.picker_dialog.view.*
import timber.log.Timber

class DialogHelper(val activity: Activity) {
    interface OnPickValue {
        fun pickValue(index: Int)
    }

    interface OnPlaceOrderOkPress {
        fun onOkPress(orderItem: PlaceOrder, dialog: Dialog)
    }

    companion object {
        val NO_RECIPIENT = -1
    }

    fun recipientPickerDialog(
        recipientList: ArrayList<Recipient>,
        valueListener: OnPickValue
    ): Dialog {
        return activity.let {
            if (recipientList.size == 0) {
                valueListener.pickValue(NO_RECIPIENT)
            } else {
                valueListener.pickValue(0)
            }
            var recipientArray = arrayOfNulls<String>(recipientList.size)
            var defaultIndex = 0
            recipientList.forEachIndexed { i, recipient ->
                recipientArray[i] = recipient.name
            }
            var pickerView = LayoutInflater.from(it)
                .inflate(R.layout.picker_dialog, null, false)
            pickerView.let {
                it.picker.displayedValues = recipientArray
                it.picker.minValue = 0
                it.picker.maxValue = recipientArray.size - 1
                it.picker.value = defaultIndex
                it.picker.wrapSelectorWheel = true
                //Make item which in picker not edit
                it.picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                it.picker.setOnValueChangedListener { picker, oldVal, newVal ->
                    valueListener.pickValue(newVal)
                }
            }
            val builder = AlertDialog.Builder(it)
            builder.setView(pickerView)
                // Add action buttons
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel,
                    { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()
        }
    }

    fun createPlaceOrderDialog(commodity: Commodity, okPress: OnPlaceOrderOkPress): Dialog {
        return activity.let {
            var orderItem = PlaceOrder()
            orderItem.itemId = commodity.id
            var orderView =
                LayoutInflater.from(it).inflate(R.layout.buyer_live_commodity_dialog, null)
            var resource = it.resources
            orderView.nameLabel.text =
                String.format(resource.getString(R.string.commodityName), commodity.name)
            orderView.descriptionLabel.text =
                String.format(
                    resource.getString(R.string.commodityDescription),
                    commodity.description
                )
            orderView.remainingLabel.text =
                String.format(
                    resource.getString(R.string.remain),
                    commodity.remainingQuantity
                )
            orderView.soldLabel.text =
                String.format(
                    resource.getString(R.string.sold),
                    commodity.soldQuantity
                )
            orderView.unitPriceLabel.text =
                String.format(
                    resource.getString(R.string.unitPrice),
                    commodity.unitPrice
                )
            Glide.with(it).load(commodity.imageUrl).into(orderView.commodityImageView)


            orderView.addBtn.setOnClickListener {
                orderItem.number++
                orderView.countLabel.text = orderItem.number.toString()
            }
            orderView.removeBtn.setOnClickListener {
                if (orderItem.number > 0) {
                    orderItem.number--
                    orderView.countLabel.text = orderItem.number.toString()
                }
            }
            orderView.pickBtn.setOnClickListener {
                recipientPickerDialog(
                    Configure.user.recipients,
                    object : OnPickValue {
                        override fun pickValue(index: Int) {
                            var recipient = Configure.user.recipients[index]
                            orderItem.recipientId = recipient.id
                            orderView.pickBtn.setText(recipient.name)
                        }
                    }
                ).show()
            }
            var builder = AlertDialog.Builder(it)
            var dialog = builder.setView(orderView)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, { dialog, which ->
                    dialog.cancel()
                }).create()
            dialog.setOnShowListener {
                Timber.d("***show listener")
                var postiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                postiveBtn.setOnClickListener {
                    okPress.onOkPress(orderItem, dialog)
                }
            }
            return dialog
        }
    }
}