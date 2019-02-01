package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Phone
import com.techapp.james.buybuygo.model.data.Recipient
import kotlinx.android.synthetic.main.common_user_dialog_recipient.view.*


class DialogHelper(val activity: Activity) {
    interface OnOkPress {
        fun onOkPress(view: View)
    }

    fun onCreateRecipientDialog(content: Recipient): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var address = content.address
            var phone = content.phone
            var customerView = LayoutInflater.from(activity)
                .inflate(R.layout.common_user_dialog_recipient, null, false)
            customerView!!.let {
                it.codeField.setText(phone.code)
                it.phoneNumberField.setText(phone.number)
                it.countryCodeField.setText(address.countryCode)
                it.postCodeField.setText(address.postCode)
                it.cityField.setText(address.city)
                it.districtField.setText(address.district)
                it.othersField.setText(address.others)
                it.nameField.setText(content.name)
            }
            builder.setView(customerView)
                // Add action buttons
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel,
                    { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun onCreateRecipientDialog(): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var customerView = LayoutInflater.from(activity)
                .inflate(R.layout.common_user_dialog_recipient, null, false)
            builder.setView(customerView)
                // Add action buttons
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                    })
            var dialog = builder.create()
            dialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }


    fun onCreatePhoneFieldDialog(content: Phone, okDoing: OnOkPress): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var customerView =
                LayoutInflater.from(activity).inflate(R.layout.common_user_dialog_recipient, null)
            customerView!!.let {
                it.codeField.setText(content.code)
                it.codeField.setText(content.number)
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