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
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Recipient
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.presenter.common.UserInfoPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_user_info.*
import timber.log.Timber

const val MODE = "Mode"

class UserInfoFragment : Fragment() {
    private var mode: Int = ExpandableAdapter.BUYER_MODE
    lateinit var dialogHelper: DialogHelper
    var userInfoPresenter: UserInfoPresenter? = null
    var expandableAdapter: ExpandableAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogHelper = DialogHelper(this.activity!!)
        userInfoPresenter = UserInfoPresenter(this)
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
            this::onCreateRecipient,
            this::onModifyRecipient
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
        //ToDo store to SQLite

        if (AreaParameter.countryWrapperList.size == 0) {
            var dialog: Dialog? = null
            var singleCountryWrapper = userInfoPresenter!!.getCountryWrappers()
            singleCountryWrapper.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    dialog = ProgressDialog.show(
                        this@UserInfoFragment.context,
                        "Loading",
                        "Waiting...",
                        true
                    )
                }
                .doOnSuccess {
                    dialog?.cancel()
                    it.body()?.let {
                        AreaParameter.countryWrapperList = it.response
                    }
                }.doOnError {
                    Timber.d("error ${it.message}")
                }.subscribe()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    fun onCreateRecipient() {
        var dialog = dialogHelper.onCreateRecipientDialog() as AlertDialog
        dialog.show()
        var countryField = dialog.findViewById<EditText>(R.id.countryField)
        countryField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    var country = it.toString()
                    AreaParameter.findCountryWrapper(country)?.let {
                        var phoneCodeField = dialog.findViewById<EditText>(R.id.codeField)!!
                        phoneCodeField.setText(it.phoneCode)
                        var countryCodeField =
                            dialog.findViewById<EditText>(R.id.countryCodeField)!!
                        countryCodeField.setText(it.countryCode)
                    }
                }
            }
        })
        var positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveBtn.setOnClickListener {
            dialog.let {
                var namefield = it.findViewById<EditText>(R.id.nameField)!!.text.toString()
                var phoneField = it.findViewById<EditText>(R.id.phoneNumberField)!!.text.toString()

                var phoneCodeField = it.findViewById<EditText>(R.id.codeField)!!.text.toString()
                var countryCodeField =
                    it.findViewById<EditText>(R.id.countryCodeField)!!.text.toString()
                var postCodeField =
                    it.findViewById<EditText>(R.id.postCodeField)!!.text.toString()
                var cityField = it.findViewById<EditText>(R.id.cityField)!!.text.toString()
                var districtField = it.findViewById<EditText>(R.id.districtField)!!.text.toString()
                var othersField = it.findViewById<EditText>(R.id.othersField)!!.text.toString()
                if (namefield.equals("") || phoneCodeField.equals("") ||
                    phoneField.equals("") || countryCodeField.equals("") ||
                    postCodeField.equals("") || cityField.equals("") ||
                    districtField.equals("") || othersField.equals("")
                    || countryCodeField.equals("")
                ) {

                } else {
                    var recipients = Recipient("", namefield)
                    recipients.phone.code = phoneCodeField
                    recipients.phone.number = phoneField
                    recipients.address.countryCode = countryCodeField
                    recipients.address.postCode = postCodeField
                    recipients.address.city = cityField
                    recipients.address.district = districtField
                    recipients.address.others = othersField
                    var singleRecipient = userInfoPresenter!!.createRecipients(recipients)
                    singleRecipient.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSuccess {
                            //                            Toast.makeText(this.context, it.body()?.string(), Toast.LENGTH_LONG)
//                                .show()
                            getUserData()
                        }
                        .doOnError {

                        }.subscribe()
                    it.dismiss()
                }
            }
        }
    }

    fun onModifyRecipient(recipient: Recipient) {
        var dialog = dialogHelper.onCreateRecipientDialog(recipient) as AlertDialog
        dialog.show()
        var postBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        postBtn.setOnClickListener {
            dialog.let {
                var name = it.findViewById<EditText>(R.id.nameField)!!.text.toString()
                var phoneCode = it.findViewById<EditText>(R.id.codeField)!!.text.toString()
                var phoneNumber = it.findViewById<EditText>(R.id.phoneNumberField)!!.text.toString()
                var countryCode = it.findViewById<EditText>(R.id.countryCodeField)!!.text.toString()
                var postCode = it.findViewById<EditText>(R.id.postCodeField)!!.text.toString()
                var city = it.findViewById<EditText>(R.id.cityField)!!.text.toString()
                var district = it.findViewById<EditText>(R.id.districtField)!!.text.toString()
                var others = it.findViewById<EditText>(R.id.othersField)!!.text.toString()
                if (name.equals("") || phoneCode.equals("") || phoneNumber.equals("")
                    || countryCode.equals("") || postCode.equals("")
                    || city.equals("") || district.equals("") || others.equals("")
                ) {

                } else {
                    dialog.cancel()
                }
            }
        }
    }

    fun getUserData() {
        var singleUser = userInfoPresenter!!.getBuyerUser()
        var dialog: Dialog? = null
        singleUser.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                dialog =
                        ProgressDialog.show(
                            this@UserInfoFragment.context,
                            "Loading",
                            "Waiting...",
                            true
                        )
            }
            .doOnSuccess {
                Configure.user = it
                expandableAdapter?.let {
                    it.data = Configure.user
                    it.notifyDataSetChanged()
                }
                dialog?.dismiss()
            }
            .subscribe()
    }

    override fun onDetach() {
        super.onDetach()
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
