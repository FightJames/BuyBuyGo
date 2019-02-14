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
import android.view.View
import com.techapp.james.buybuygo.presenter.Configure
import com.techapp.james.buybuygo.view.buyer.BuyerActivity
import com.techapp.james.buybuygo.view.seller.activity.seller.SellerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList


class ChooseActivity : BaseActivity(), com.techapp.james.buybuygo.view.View {
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

    override fun onPause() {
        super.onPause()
    }

    fun init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        Glide.with(this)
            .load(R.drawable.buyer_seller)
            .into(backgroundImageView)
        choosePresenter = ChoosePresenter(this)
        buyerTextView.setOnClickListener {
            var singleUser = choosePresenter!!.chooseBuyer()
            singleUser.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    sellerTextView.visibility = View.INVISIBLE
                    buyerTextView.visibility = View.INVISIBLE
                    loadUserDataprogressBar.visibility = View.VISIBLE
                }
                .doOnSuccess {
                    var i = Intent(this, BuyerActivity::class.java)
                    startActivity(i)
                }.subscribe()
        }
        sellerTextView.setOnClickListener {
            var singleUser = choosePresenter!!.chooseSeller()
            singleUser
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    sellerTextView.visibility = View.INVISIBLE
                    buyerTextView.visibility = View.INVISIBLE
                    loadUserDataprogressBar.visibility = View.VISIBLE
                }.doOnSuccess {
                    var i = Intent(this, SellerActivity::class.java)
                    startActivity(i)
                }.subscribe()
        }
        applyRight()
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle?) {
        outState.putSerializable(BACKUP_USER, Configure.user)
        Timber.d("/*/* save user")
        super.onSaveInstanceState(outState, outPersistentState)
    }
// it will call by system scenario, not user scenario ( user back app)
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
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
        val BACKUP_USER = "USER"
    }
}
