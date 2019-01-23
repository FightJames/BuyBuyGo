package com.techapp.james.buybuygo.view.choose

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
import android.net.Uri
import com.techapp.james.buybuygo.presenter.Configure
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class ChooseActivity : BaseActivity() {
    var choosePresenter: ChoosePresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        init()



//        val requestBody: MultipartBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("name", c.name)
//                .addFormDataPart("description", c.description)
//                .addFormDataPart("stock", c.stock.toString())
//                .addFormDataPart("cost", c.cost.toString())
//                .addFormDataPart("unit_price", c.unit_price.toString())
//                .addFormDataPart("images", "Ray", requestFile)
//                .build()



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
    }

    override fun onDestroy() {
        super.onDestroy()
        choosePresenter = null
    }



}
