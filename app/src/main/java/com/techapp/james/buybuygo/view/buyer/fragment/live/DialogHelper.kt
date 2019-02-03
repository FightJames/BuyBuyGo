package com.techapp.james.buybuygo.view.buyer.fragment.live

import android.app.Activity
import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import kotlinx.android.synthetic.main.picker_dialog.view.*

class DialogHelper(val activity: Activity) {
    interface OnPickValue {
        fun pickValue(index: Int)
    }

    fun recipientPickerDialog(
        recipientList: ArrayList<Recipient>,
        valueListener: OnPickValue
    ): Dialog {
        return activity.let {
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

    fun createPlaceOrderDialog() {
        return activity.let {
            var orderView =
                LayoutInflater.from(it).inflate(R.layout.buyer_live_commodity_dialog, null)
        }
    }
}