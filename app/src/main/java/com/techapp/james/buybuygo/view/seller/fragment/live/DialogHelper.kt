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
import android.view.ViewGroup
import kotlinx.android.synthetic.main.seller_live_dialog_token.view.*


class DialogHelper {
    var fragment: Fragment? = null

    constructor(fragment: Fragment) {
        this.fragment = fragment
    }

    fun createDialog(): AlertDialog {
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

    fun createTokenDialog(activity: Activity, token: String): Dialog {
        val builder = AlertDialog.Builder(activity)
        var tokenView =
            LayoutInflater.from(activity).inflate(R.layout.seller_live_dialog_token, null)
        builder.setView(tokenView)
        builder.setTitle("Please Click Token")
        val showText = tokenView.tokenLabel
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
            manager!!.primaryClip = clipData
        }
//        showText.setPadding()
        showText.setTextIsSelectable(true)
        return builder.create()
    }

}