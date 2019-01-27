package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Address
import com.techapp.james.buybuygo.model.data.Phone
import kotlinx.android.synthetic.main.common_user_dialog_address.view.*
import kotlinx.android.synthetic.main.user_common_dialog.view.*
import kotlinx.android.synthetic.main.user_common_dialog_phone.view.*


class DialogHelper(val activity: Activity) {

    private fun onCreateAddressFieldDialog(content: Address, okDoing: DialogInterface.OnClickListener): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var customerView = LayoutInflater.from(activity).inflate(R.layout.common_user_dialog_address, null)
            customerView!!.let {
                it.countryCodeField.setText(content.countryCode)
                it.postCodeField.setText(content.postCode)
                it.cityField.setText(content.city)
                it.districtField.setText(content.district)
                it.othersField.setText(content.others)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok, okDoing)
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onCreateOneFieldDialog(content: String, okDoing: DialogInterface.OnClickListener): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var customerView = LayoutInflater.from(activity).inflate(R.layout.user_common_dialog, null)
            customerView!!.let {
                it.field.setText(content)
            }

            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok, okDoing)
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onCreatePhoneFieldDialog(content: Phone, okDoing: DialogInterface.OnClickListener): Dialog {
        return activity.let {
            val builder = AlertDialog.Builder(it)
            var customerView = LayoutInflater.from(activity).inflate(R.layout.common_user_dialog_address, null)
            customerView!!.let {
                it.phoneCodeEditText.setText(content.code)
                it.phoneCodeEditText.setText(content.number)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok, okDoing)
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}