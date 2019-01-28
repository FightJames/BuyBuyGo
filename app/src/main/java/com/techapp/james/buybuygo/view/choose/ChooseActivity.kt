package com.techapp.james.buybuygo.view.choose

import android.Manifest
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Commodity
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.ChoosePresenter
import com.techapp.james.buybuygo.view.BaseActivity
import kotlinx.android.synthetic.main.activity_choose.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import android.R.attr.path
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.techapp.james.buybuygo.presenter.Configure
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.ArrayList


class ChooseActivity : BaseActivity() {
    var choosePresenter: ChoosePresenter? = null
    private val allPermission = ArrayList<Int>()
    private val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)  //權限項目在此新增
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    this.requestPermissions(permission,
                            REQUEST_CODE_ASK_PERMISSIONS)
                }
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

    }

    companion object {
        val REQUEST_CODE_ASK_PERMISSIONS = 0
    }
}
