package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.CountryWrapper
import com.techapp.james.buybuygo.model.data.buyer.Phone
import kotlinx.android.synthetic.main.common_user_dialog_recipient.view.*
import kotlinx.android.synthetic.main.picker_dialog.view.*


class DialogHelper(val activity: Activity) {
    interface OnOkPress {
        fun onOkPress(view: View)
    }

    interface OnPickValue {
        fun pickValue(countryName: String)
    }

    fun createCountryPickerDialog(
        countryWrappers: ArrayList<CountryWrapper>,
        valueListener: OnPickValue
    ): Dialog {
        return activity.let {
            var countryArray = arrayOfNulls<String>(countryWrappers.size)
            var defaultIndex = 0
            for (i in countryWrappers.indices) {
                countryArray[i] = countryWrappers[i].country
                if (countryArray[i].equals("Taiwan")) {
                    defaultIndex = i
                }
            }

            var pickerView = LayoutInflater.from(it)
                .inflate(R.layout.picker_dialog, null, false)
            pickerView.let {
                it.picker.displayedValues = countryArray
                it.picker.minValue = 0
                it.picker.maxValue = countryArray.size - 1
                it.picker.value = defaultIndex
                it.picker.wrapSelectorWheel = true
                it.picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                it.picker.setOnValueChangedListener { picker, oldVal, newVal ->
                    valueListener.pickValue(countryArray[newVal]!!)
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

    fun createRecipientDialog(s: String): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var customerView = LayoutInflater.from(activity)
                .inflate(R.layout.common_user_dialog_recipient, null, false)
            builder.setView(customerView)
                // Add action buttons
                .setPositiveButton(s, null)
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
            var dialog = builder.create()
            dialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun createPhoneFieldDialog(content: Phone, okDoing: OnOkPress): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var customerView =
                LayoutInflater.from(activity).inflate(R.layout.common_user_dialog_recipient, null)
            customerView!!.let {
                it.phoneCodeLabel.setText(content.code)
                it.phoneCodeLabel.setText(content.number)
            }
            builder.setView(customerView)
                // Add action buttons
                .setPositiveButton(R.string.ok, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        okDoing.onOkPress(customerView)
                    }

                })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}