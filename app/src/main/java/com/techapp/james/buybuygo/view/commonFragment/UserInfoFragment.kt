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
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.buyer.CountryWrapper
import com.techapp.james.buybuygo.model.data.buyer.Recipient
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.presenter.common.UserInfoPresenter
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
        var countryWrapper: CountryWrapper? = null
        var countryBtn = dialog.findViewById<Button>(R.id.countryBtn)
        var phoneCodeLabel = dialog.findViewById<TextView>(R.id.phoneCodeField)!!
        var countryCodeLabel = dialog.findViewById<TextView>(R.id.countryCodeField)!!
        var cityField = dialog.findViewById<EditText>(R.id.cityField)
        var districtField = dialog.findViewById<EditText>(R.id.districtField)
        var postCodeField = dialog.findViewById<EditText>(R.id.postCodeField)
        countryBtn!!.setOnClickListener {
            var pickerDialog =
                dialogHelper.createCountryPickerDialog(AreaParameter.countryWrapperList,
                    object : DialogHelper.OnPickValue {
                        override fun pickValue(name: String) {
                            Timber.d("pickCountryName $name")
                            countryWrapper = AreaParameter.findCountryWrapperByCountry(name)
                        }
                    },
                    object : DialogHelper.OnOkPress {
                        override fun onOkPress(view: View) {
                            //pick area
                            countryWrapper?.let {
                                countryBtn.text = it.country
                                phoneCodeLabel.text = it.phoneCode
                                countryCodeLabel.text = it.countryCode
                            }
                            if (countryWrapper!!.countryCode.equals("TW")) {
                                postCodeField?.isEnabled = false
                                cityField?.setText("")
                                districtField?.setText("")
                                cityField?.setOnTouchListener { v, event ->
                                    if (event.action == MotionEvent.ACTION_DOWN) {
                                        dialogHelper.createAreaPickerDialog(
                                            AreaParameter.areaWrapperList,
                                            true,
                                            object : DialogHelper.OnPickValue {
                                                override fun pickValue(name: String) {
                                                    cityField.setText(name)
                                                }
                                            }
                                        )?.show()
                                    }
                                    true
                                }
                                districtField?.setOnTouchListener { v, event ->
                                    if (event.action == MotionEvent.ACTION_DOWN) {
                                        cityField?.let {
                                            var areaList =
                                                AreaParameter.findRelationAreaWrappers(
                                                    it.text.toString()
                                                )
                                            if (areaList.size != 0) {
                                                dialogHelper.createAreaPickerDialog(
                                                    areaList,
                                                    false,
                                                    object : DialogHelper.OnPickValue {
                                                        override fun pickValue(name: String) {
                                                            districtField?.setText(name)
                                                            var targetWrapper =
                                                                AreaParameter.findAreaWrappers(
                                                                    it.text.toString(),
                                                                    name
                                                                )
                                                            targetWrapper?.let {
                                                                postCodeField?.setText(
                                                                    it.zipCode.toString()
                                                                )
                                                            }
                                                        }
                                                    }
                                                )?.show()
                                            }
                                        }
                                    }
                                    true
                                }
                            } else {
                                cityField?.setOnTouchListener { v, event -> false }
                                districtField?.setOnTouchListener { v, event -> false }
                                postCodeField?.isEnabled = true
                            }
                        }
                    })
            pickerDialog!!.show()
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
                it.findViewById<TextView>(R.id.phoneCodeField)!!.setText(recipient!!.phone.code)
                var address = recipient.address
                it.findViewById<EditText>(R.id.postCodeField)!!.setText(address.postCode)
                it.findViewById<TextView>(R.id.countryCodeField)!!.setText(address.countryCode)
                it.findViewById<EditText>(R.id.cityField)!!.setText(address.city)
                it.findViewById<EditText>(R.id.districtField)!!.setText(address.district)
                it.findViewById<EditText>(R.id.othersField)!!.setText(address.others)
                var countryWrapper =
                    AreaParameter.findCountryWrapperByCountryCode(address.countryCode)
                countryBtn.text = countryWrapper.country
                if (address.countryCode.equals("TW")) {
                    it.findViewById<EditText>(R.id.postCodeField)!!.isEnabled = false
                    it.findViewById<TextView>(R.id.countryCodeField)!!.isEnabled = false
                    cityField?.setOnTouchListener { v, event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            dialogHelper.createAreaPickerDialog(
                                AreaParameter.areaWrapperList,
                                true,
                                object : DialogHelper.OnPickValue {
                                    override fun pickValue(name: String) {
                                        cityField.setText(name)
                                    }
                                }
                            )?.show()
                        }
                        true
                    }
                    districtField?.setOnTouchListener { v, event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            cityField?.let {
                                var areaList =
                                    AreaParameter.findRelationAreaWrappers(
                                        it.text.toString()
                                    )
                                if (areaList.size != 0) {
                                    dialogHelper.createAreaPickerDialog(
                                        areaList,
                                        false,
                                        object : DialogHelper.OnPickValue {
                                            override fun pickValue(name: String) {
                                                districtField?.setText(name)
                                                var targetWrapper =
                                                    AreaParameter.findAreaWrappers(
                                                        it.text.toString(),
                                                        name
                                                    )
                                                targetWrapper?.let {
                                                    postCodeField?.setText(
                                                        it.zipCode.toString()
                                                    )
                                                }
                                            }
                                        }
                                    )?.show()
                                }
                            }
                        }
                        true
                    }
                }
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

    fun createDialogOkPress(it: Dialog, countryWrapper: CountryWrapper?) {
        var namefield = it.findViewById<EditText>(R.id.nameField)!!.text.toString()
        var phoneField = it.findViewById<EditText>(R.id.phoneNumberField)!!.text.toString()
        var postCodeField =
            it.findViewById<EditText>(R.id.postCodeField)!!.text.toString()
        var cityField = it.findViewById<EditText>(R.id.cityField)!!.text.toString()
        var districtField = it.findViewById<EditText>(R.id.districtField)!!.text.toString()
        var othersField = it.findViewById<EditText>(R.id.othersField)!!.text.toString()
        var recipients = Recipient("", namefield)
        countryWrapper?.let {
            recipients.phone.code = it.phoneCode
            recipients.address.countryCode = it.countryCode
        }
        recipients.phone.number = phoneField
        recipients.address.postCode = postCodeField
        recipients.address.city = cityField
        recipients.address.district = districtField
        recipients.address.others = othersField
        userInfoPresenter!!.createRecipients(recipients, {
            it.dismiss()
        })
//        }
    }

    fun modifyDialogOkPress(it: Dialog, countryWrapper: CountryWrapper?, recipient: Recipient) {
        var namefield = it.findViewById<EditText>(R.id.nameField)!!.text.toString()
        var phoneField = it.findViewById<EditText>(R.id.phoneNumberField)!!.text.toString()
        var postCodeField =
            it.findViewById<EditText>(R.id.postCodeField)!!.text.toString()
        var cityField = it.findViewById<EditText>(R.id.cityField)!!.text.toString()
        var districtField = it.findViewById<EditText>(R.id.districtField)!!.text.toString()
        var othersField = it.findViewById<EditText>(R.id.othersField)!!.text.toString()
        recipient.name = namefield
        countryWrapper?.let {
            recipient.phone.code = it.phoneCode
            recipient.address.countryCode = it.countryCode
        }
        recipient.phone.number = phoneField
        recipient.address.postCode = postCodeField
        recipient.address.city = cityField
        recipient.address.district = districtField
        recipient.address.others = othersField
        userInfoPresenter!!.modifyRecipient(recipient, {
            it.cancel()
        })
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
