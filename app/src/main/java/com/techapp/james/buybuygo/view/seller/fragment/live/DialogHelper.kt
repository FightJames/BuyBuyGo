package com.techapp.james.buybuygo.view.seller.fragment.live

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.techapp.james.buybuygo.R
import android.widget.TextView
import android.widget.Toast
import android.content.Context.CLIPBOARD_SERVICE
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService


class DialogHelper {
    var fragment: Fragment? = null

    constructor(fragment: Fragment) {
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

    fun onCreateTokenDialog(activity: Activity, token: String): Dialog {
        val builder = AlertDialog.Builder(activity)
        val showText = TextView(activity)
        showText.text = token

//        showText.textSize =
        showText.setOnClickListener {
            val manager =
                activity!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?


            var clipData = ClipData.newPlainText(null, showText.text)
            Toast.makeText(
                activity, "Text in clipboard",
                Toast.LENGTH_SHORT
            ).show()
            showText.isScrollbarFadingEnabled
            manager!!.primaryClip = clipData
        }
//        showText.setPadding()
        showText.setTextIsSelectable(true)
        builder.setView(showText)
            .setTitle("Your Token")
            .setPositiveButton(R.string.ok, null)

        return builder.create()
    }

    fun destroy() {
        fragment = null
    }

    interface OnPressOK {
        fun onPressOK(view: View)
    }
}