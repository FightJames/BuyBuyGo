package com.techapp.james.buybuygo.view.seller.fragment.commodity

import android.app.Dialog
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.Commodity
import com.techapp.james.buybuygo.model.file.FileData
import com.techapp.james.buybuygo.model.networkManager.NetworkManager
import com.techapp.james.buybuygo.presenter.seller.CommodityPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commodity.*
import kotlinx.android.synthetic.main.seller_fragment_commodity_dialog.view.*
import timber.log.Timber

class DialogHelper {
    var fragment: Fragment?
    var intentCamera: (() -> Unit)?
    var fileData: FileData?
    lateinit var customerView: View

    constructor(
        fragment: CommodityFragment,
        intentCamera: () -> Unit,
        fileData: FileData
    ) {
        this.fragment = fragment
        this.intentCamera = intentCamera
        this.fileData = fileData
    }

    interface CreateCallback {
        fun onCreate(c: Commodity, fileData: FileData)
    }

    fun createDialog(createCallback: CreateCallback? = null): Dialog {
        return fragment!!.activity?.let {
            val builder = AlertDialog.Builder(it)
            customerView =
                    LayoutInflater.from(it).inflate(R.layout.seller_fragment_commodity_dialog, null)
            customerView!!.commodityImageView.setOnClickListener {
                intentCamera!!.invoke()
            }
            builder.setView(customerView)
                // Add action buttons
                .setPositiveButton(R.string.ok, { dialog, id ->
                    var pattern = "^[0-9]*\$".toRegex()
                    var stock = customerView!!.remainingField.text.toString()
                    var cost = customerView!!.costField.text.toString()
                    var unitPrice = customerView!!.unitPriceField.text.toString()
                    Timber.d("stock" + stock.equals("").toString())
                    var stockFlag = pattern.matches(stock) && !stock.equals("")
                    var costFlag = pattern.matches(cost) && !cost.equals("")
                    var unitPriceFlag = pattern.matches(unitPrice) && !unitPrice.equals("")
                    customerView!!.unitPriceField.text.toString()
                    var commodity = Commodity(
                        "",
                        customerView!!.nameField.text.toString(),
                        customerView!!.descriptionField.text.toString(),
                        if (stockFlag) stock.toInt() else 0,
                        if (costFlag) cost.toInt() else 0,
                        if (unitPriceFlag) unitPrice.toInt() else 0,
                        fileData!!.fileUri.toString()
                    )

                    createCallback?.onCreate(commodity, fileData!!)
//                    var insOb = presenter!!.insertItem(commodity, fileData!!)
//                    insOb.subscribeOn(Schedulers.newThread())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .doOnSubscribe {
//                            fragment!!.loadItemProgressBar.visibility = View.VISIBLE
//                        }
//                        .doOnSuccess {
//                            Timber.d("success message " + it.message())
//                            Timber.d("success message " + it.errorBody()?.string())
//                            presenter!!.getUploadItem()
//                        }
//                        .doOnError {
//                            Timber.d("error " + it.message)
//                        }
//                        .subscribe()
                })
                .setNegativeButton(R.string.cancel,
                    { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface ModifyCallBack {
        fun onModify(c: Commodity,fileData: FileData)
    }

    fun createModifyDialog(commodity: Commodity, modifyCallBack: ModifyCallBack? = null): Dialog {
        return fragment!!.activity?.let {
            val builder = AlertDialog.Builder(it)
            customerView =
                    LayoutInflater.from(it).inflate(R.layout.seller_fragment_commodity_dialog, null)
            customerView!!.let {
                it.nameField.setText(commodity.name)
                it.descriptionField.setText(commodity.description)
                it.remainingField.setText(commodity.stock.toString())
                it.costField.setText(commodity.cost.toString())
                it.unitPriceField.setText(commodity.unitPrice.toString())
                Glide.with(it).load(commodity.imageUrl).into(it.commodityImageView)
            }

            ConfigUpdatePhoto.IS_UPDATE_PHOTO = false
            customerView!!.commodityImageView.setOnClickListener {
                intentCamera!!.invoke()
            }
            builder.setView(customerView)
                // Add action buttons
                .setPositiveButton(R.string.ok,
                    { dialog, id ->
                        Timber.d("name " + customerView!!.nameField.text.toString())
                        var pattern = "^[0-9]*\$".toRegex()
                        var stock = customerView!!.remainingField.text.toString()
                        var cost = customerView!!.costField.text.toString()
                        var unitPrice = customerView!!.unitPriceField.text.toString()
                        var stockFlag = pattern.matches(stock)
                        var costFlag = pattern.matches(cost)
                        var unitPriceFlag = pattern.matches(unitPrice)
                        customerView!!.unitPriceField.text.toString()
                        commodity.name = customerView!!.nameField.text.toString()
                        commodity.description = customerView!!.descriptionField.text.toString()
                        commodity.stock = if (stockFlag) stock.toInt() else 0
                        commodity.cost = if (costFlag) cost.toInt() else 0
                        commodity.unitPrice = if (unitPriceFlag) unitPrice.toInt() else 0
                        commodity.isModifyImage = ConfigUpdatePhoto.IS_UPDATE_PHOTO
modifyCallBack?.onModify(commodity,fileData!!)
//                        var updateOB = presenter!!.updateItem(commodity, fileData!!)
//                        if (ConfigUpdatePhoto.IS_UPDATE_PHOTO) {
//
//                            modifyCallBack?.onModify()
//
//                            updateOB.subscribeOn(Schedulers.newThread())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .doOnSuccess {
//                                    presenter!!.getUploadItem()
//                                }.doOnError {
//                                }.subscribe()
//
//                        } else {
//                            var singleFile = NetworkManager
//                                .downloadImage(commodity.imageUrl, fileData!!.path)
//                            singleFile.subscribeOn(Schedulers.newThread())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .doOnSuccess {
//                                    updateOB.subscribeOn(Schedulers.newThread())
//                                        .observeOn(AndroidSchedulers.mainThread())
//                                        .doOnSuccess {
//                                            presenter!!.getUploadItem()
//                                        }.doOnError {
//                                        }.subscribe()
//                                }.subscribe()
//                        }
                    })
                .setNegativeButton(R.string.cancel,
                    { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()


        } ?: throw IllegalStateException("Activity cannot be null")
    }
}