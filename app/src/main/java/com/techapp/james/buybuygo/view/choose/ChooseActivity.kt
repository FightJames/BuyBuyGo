package com.techapp.james.buybuygo.view.choose

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.presenter.ChoosePresenter
import com.techapp.james.buybuygo.view.BaseActivity
import kotlinx.android.synthetic.main.activity_choose.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.PersistableBundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.techapp.james.buybuygo.model.data.common.UserStatus
import com.techapp.james.buybuygo.view.buyer.activity.BuyerActivity
import com.techapp.james.buybuygo.view.seller.activity.seller.SellerActivity
import timber.log.Timber
import java.util.ArrayList


class ChooseActivity : BaseActivity(), ChooseView {

    var choosePresenter: ChoosePresenter? = null
    private val allPermission = ArrayList<Int>()
    private val permission = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )  //權限項目在此新增


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        savedInstanceState?.let {
//            Configure.user = it.getSerializable(BACKUP_USER) as User
//        }
        Timber.d("/*/* restore user")
        setContentView(R.layout.activity_choose)
        init()
    }

    override fun onResume() {
        super.onResume()
        sellerTextView.visibility = View.VISIBLE
        buyerTextView.visibility = View.VISIBLE
        loadUserDataprogressBar.visibility = View.GONE
    }

    fun init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        Glide.with(this)
            .load(R.drawable.buyer_seller)
            .into(backgroundImageView)
        choosePresenter = ChoosePresenter(this)
        buyerTextView.setOnClickListener {
            choosePresenter!!.chooseBuyer()
        }

        sellerTextView.setOnClickListener {
            choosePresenter!!.chooseSeller()
        }
        applyRight()
        choosePresenter!!.getUserStatus()
    }

    override fun isLoad(flag: Boolean) {
        if (flag) {
            sellerTextView.visibility = View.INVISIBLE
            buyerTextView.visibility = View.INVISIBLE
            loadUserDataprogressBar.visibility = View.VISIBLE
        } else {
            sellerTextView.visibility = View.VISIBLE
            buyerTextView.visibility = View.VISIBLE
            loadUserDataprogressBar.visibility = View.INVISIBLE
        }
    }

    override fun autoIntent(userStatus: UserStatus) {
        if (userStatus.host == 0) {
            choosePresenter!!.chooseBuyer()
        } else {
            choosePresenter!!.chooseSeller()
        }

    }

    override fun intentToBuyer() {
        var i = Intent(this, BuyerActivity::class.java)
        startActivity(i)
    }

    override fun intentToSeller() {
        var i = Intent(this, SellerActivity::class.java)
        startActivity(i)
    }

    // it will call by system scenario, not user scenario ( user back app)
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun continueOperation(okPress: () -> Unit, cancelPress: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Continue last operation ?")
            .setPositiveButton(R.string.ok, { dialog, which ->
                okPress.invoke()
            })
            .setNegativeButton(R.string.cancel, { dialog, which ->
                cancelPress.invoke()
                dialog.cancel()
            })
        val dialog = builder.create()
        dialog.show()
    }

    override fun showRequestMessage(message: String) {
        Toast.makeText(
            this@ChooseActivity,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        choosePresenter = null
    }

    fun applyRight(): Boolean {
        for (i in permission.indices) {
            val eachPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.checkSelfPermission(permission[i])
            } else {
                PackageManager.PERMISSION_GRANTED
            } //命名權限
            allPermission.add(eachPermission)
        }
        for (i in allPermission.indices) {
            if (allPermission[i] != PackageManager.PERMISSION_GRANTED) {
                // System.out.println(permission[serviceIntent]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(
                        permission,
                        REQUEST_CODE_ASK_PERMISSIONS
                    )
                }
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }

    companion object {
        val REQUEST_CODE_ASK_PERMISSIONS = 0
    }
}
