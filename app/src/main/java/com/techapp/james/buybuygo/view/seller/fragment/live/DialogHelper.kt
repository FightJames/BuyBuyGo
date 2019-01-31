package com.techapp.james.buybuygo.view.seller.fragment.live

import android.app.Dialog
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.techapp.james.buybuygo.R

class DialogHelper {
    var fragment: LiveFragment? = null

    constructor(fragment: LiveFragment) {
        this.fragment = fragment
    }

    fun onCreateDialog(): AlertDialog {
        return fragment!!.activity?.let {
            val builder = AlertDialog.Builder(it)
            var customerView =
                LayoutInflater.from(it).inflate(R.layout.seller_live_description_dialog, null)
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

    fun destroy() {
        fragment = null
    }

    interface OnPressOK {
        fun onPressOK(view: View)
    }
}