package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.model.data.buyer.CountryWrapper
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.presenter.common.UserInfoPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_user_info.*
import timber.log.Timber

const val MODE = "Mode"

class UserInfoFragment : Fragment(), ExpandableAdapter.ItemClick,
    UserInfoView {

    private var mode: Int = ExpandableAdapter.BUYER_MODE
    lateinit var dialogHelper: DialogHelper
    var userInfoPresenter: UserInfoPresenter? = null
    var expandableAdapter: ExpandableAdapter? = null
    lateinit var loadDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogHelper = DialogHelper(this.activity!!)
        userInfoPresenter = UserInfoPresenter(this)
        loadDialog = ProgressDialog(this.activity)
        arguments?.let {
            mode = it.getInt(MODE)
        }
    }

    override fun onStart() {
        super.onStart()
        userInfoList.layoutManager = LinearLayoutManager(this.activity)
        expandableAdapter = ExpandableAdapter(
            this.activity as Activity,
            mode,
            Configure.user,
            this
        )
        userInfoList.adapter = expandableAdapter
        var itemDecoration = DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                this.activity as Context,
                R.drawable.user_info_divider_shape
            )!!
        )
        userInfoList.addItemDecoration(itemDecoration)
        //get countryWrapperList

        if (AreaParameter.countryWrapperList.size == 0) {
            userInfoPresenter!!.getCountryWrappers()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun createRecipient() {
        stateDialogCreateModify(true)
    }

    fun createShellDialog(postiveBtn: String): Dialog {
        return dialogHelper.createRecipientDialog(postiveBtn) as AlertDialog
    }

    override fun isLoadWholeView(flag: Boolean) {
        if (flag) {
            loadDialog.setMessage("Loading...")
            loadDialog.show()
        } else {
            loadDialog.cancel()
        }
    }

    override fun showRequestMessage(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    override fun updateUserInfo() {
        expandableAdapter?.let {
            it.data = Configure.user
            it.notifyDataSetChanged()
        }
    }

    fun stateDialogCreateModify(
        isCreate: Boolean,
        recipient: Recipient? = null
    ) {// true is create dialog ,false is modify dialog
        var dialog: Dialog
        if (isCreate)
            dialog = createShellDialog(resources.getString(R.string.ok)) as AlertDialog
        else
            dialog = createShellDialog("modify") as AlertDialog
        dialog.show()
        var countryWrapper = AreaParameter.findCountryWrapper("Taiwan")
        var countryBtn = dialog.findViewById<Button>(R.id.countryBtn)
        countryBtn!!.setOnClickListener {
            var pickerDialog =
                dialogHelper.createCountryPickerDialog(AreaParameter.countryWrapperList,
                    object : DialogHelper.OnPickValue {
                        override fun pickValue(countryName: String) {
                            Timber.d("pickCountryName $countryName")
                            countryWrapper = AreaParameter.findCountryWrapper(countryName)
                        }
                    })
            pickerDialog!!.show()
            (pickerDialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
                    dialog.findViewById<TextView>(R.id.phoneCodeLabel)!!.text =
                            countryWrapper?.phoneCode
                    dialog.findViewById<TextView>(R.id.countryCodeLabel)!!.text =
                            countryWrapper?.countryCode
                    pickerDialog.cancel()
                }
        }
        if (isCreate) {
            var positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveBtn.setOnClickListener {
                createDialogOkPress(dialog, countryWrapper)
            }
        } else {
            dialog.let {
                it.findViewById<EditText>(R.id.nameField)!!.setText(recipient!!.name)
                it.findViewById<EditText>(R.id.phoneNumberField)!!.setText(recipient!!.phone.number)
                it.findViewById<TextView>(R.id.phoneCodeLabel)!!.setText(recipient!!.phone.code)
                var address = recipient.address
                it.findViewById<EditText>(R.id.postCodeField)!!.setText(address.postCode)
                it.findViewById<TextView>(R.id.countryCodeLabel)!!.setText(address.countryCode)
                it.findViewById<EditText>(R.id.cityField)!!.setText(address.city)
                it.findViewById<EditText>(R.id.districtField)!!.setText(address.district)
                it.findViewById<EditText>(R.id.othersField)!!.setText(address.others)
            }
            var positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveBtn.setOnClickListener {
                modifyDialogOkPress(dialog, countryWrapper, recipient!!)
            }
        }
    }

    override fun deleteRecipient(recipient: Recipient) {
        userInfoPresenter!!.deleteRecipients(recipient)
    }

    override fun modifyRecipient(recipient: Recipient) {
        stateDialogCreateModify(false, recipient)
    }

    fun createDialogOkPress(it: Dialog, countryWrapper: CountryWrapper) {
        var namefield = it.findViewById<EditText>(R.id.nameField)!!.text.toString()
        var phoneField = it.findViewById<EditText>(R.id.phoneNumberField)!!.text.toString()
        var postCodeField =
            it.findViewById<EditText>(R.id.postCodeField)!!.text.toString()
        var cityField = it.findViewById<EditText>(R.id.cityField)!!.text.toString()
        var districtField = it.findViewById<EditText>(R.id.districtField)!!.text.toString()
        var othersField = it.findViewById<EditText>(R.id.othersField)!!.text.toString()
        if (namefield.equals("") || phoneField.equals("") ||
            postCodeField.equals("") || cityField.equals("") ||
            districtField.equals("") || othersField.equals("")
        ) {
        } else {
            var recipients = Recipient("", namefield)
            recipients.phone.code = countryWrapper!!.phoneCode
            recipients.phone.number = phoneField
            recipients.address.countryCode = countryWrapper!!.countryCode
            recipients.address.postCode = postCodeField
            recipients.address.city = cityField
            recipients.address.district = districtField
            recipients.address.others = othersField
            userInfoPresenter!!.createRecipients(recipients, {
                it.dismiss()
            })
        }
    }

    fun modifyDialogOkPress(it: Dialog, countryWrapper: CountryWrapper, recipient: Recipient) {
        var namefield = it.findViewById<EditText>(R.id.nameField)!!.text.toString()
        var phoneField = it.findViewById<EditText>(R.id.phoneNumberField)!!.text.toString()
        var postCodeField =
            it.findViewById<EditText>(R.id.postCodeField)!!.text.toString()
        var cityField = it.findViewById<EditText>(R.id.cityField)!!.text.toString()
        var districtField = it.findViewById<EditText>(R.id.districtField)!!.text.toString()
        var othersField = it.findViewById<EditText>(R.id.othersField)!!.text.toString()
        if (namefield.equals("") || phoneField.equals("") ||
            postCodeField.equals("") || cityField.equals("") ||
            districtField.equals("") || othersField.equals("")
        ) {
        } else {
            recipient.name = namefield
            recipient.phone.code = countryWrapper!!.phoneCode
            recipient.phone.number = phoneField
            recipient.address.countryCode = countryWrapper!!.countryCode
            recipient.address.postCode = postCodeField
            recipient.address.city = cityField
            recipient.address.district = districtField
            recipient.address.others = othersField
            userInfoPresenter!!.modifyRecipient(recipient)
            it.cancel()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(mode: Int) =
            UserInfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(MODE, mode)
                }
            }
    }
}
