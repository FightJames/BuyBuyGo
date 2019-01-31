package com.techapp.james.buybuygo.view.commonFragment

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Phone
import com.techapp.james.buybuygo.model.data.Recipients
import com.techapp.james.buybuygo.model.data.User
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.presenter.common.UserInfoPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_user_info.*
import kotlinx.android.synthetic.main.user_common_dialog_recipient.*

const val MODE = "Mode"

class UserInfoFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
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
//        var u = User("sdlfj", "sdlfj", "sdlfj", "sdlfj", arrayListOf(Recipients("sdlfj", "sdlfj"), Recipients("sdlfj", "sdlfj")))
        expandableAdapter = ExpandableAdapter(
            this.activity as Activity,
            mode,
            Configure.user,
            this::onCreateRecipient
        )
        userInfoList.adapter = expandableAdapter
//        userInfoList.adapter = ExpandableAdapter(this.activity as Activity, u)
        var itemDecoration = DividerItemDecoration(this.activity, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                this.activity as Context,
                R.drawable.user_info_divider_shape
            )!!
        )

        userInfoList.addItemDecoration(itemDecoration)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun onCreateRecipient() {
        var dialog = dialogHelper.onCreateRecipientDialog() as AlertDialog
        dialog.show()
        var positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveBtn.setOnClickListener {
            dialog.let {
                var namefield = it.findViewById<EditText>(R.id.nameField)!!.text.toString()
                var phoneCodeField = it.findViewById<EditText>(R.id.codeField)!!.text.toString()
                var phoneField = it.findViewById<EditText>(R.id.phoneNumberField)!!.text.toString()
                var countryCodeField =
                    it.findViewById<EditText>(R.id.countryCodeField)!!.text.toString()
                var postCodeField =
                    it.findViewById<EditText>(R.id.countryCodeField)!!.text.toString()
                var cityField = it.findViewById<EditText>(R.id.cityField)!!.text.toString()
                var districtField = it.findViewById<EditText>(R.id.districtField)!!.text.toString()
                var othersField = it.findViewById<EditText>(R.id.othersField)!!.text.toString()
                if (namefield.equals("") || phoneCodeField.equals("") ||
                    phoneField.equals("") || countryCodeField.equals("") ||
                    postCodeField.equals("") || cityField.equals("") ||
                    districtField.equals("") || othersField.equals("")
                ) {

                } else {
                    var recipients = Recipients("", namefield)
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
                            Toast.makeText(this.context, it.body()?.string(), Toast.LENGTH_LONG)
                                .show()
                            getUserData()
                        }
                        .doOnError {

                        }.subscribe()
                    it.dismiss()
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
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
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
