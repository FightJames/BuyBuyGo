package com.techapp.james.buybuygo.view.seller.fragment.commodity

import android.app.Activity
import android.app.Dialog
import android.support.v7.app.AlertDialog

class DialogHelper {


    private fun onCreateDialog(activity: Activity): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            customerView = layoutInflater.inflate(R.layout.seller_fragment_commodity_dialog, null)
            customerView!!.commodityImageView.setOnClickListener {
                ConfigUpdatePhoto.IS_UPDATE_PHOTO = true
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileData.fileUri)
                startActivityForResult(i, CAMERA_RESULT)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok, { dialog, id ->
                        Timber.d("name " + customerView!!.nameField.text.toString())
                        var pattern = "^[0-9]*\$".toRegex()
                        var stock = customerView!!.stockField.text.toString()
                        var cost = customerView!!.costField.text.toString()
                        var unitPrice = customerView!!.unitPriceField.text.toString()
                        var stockFlag = pattern.matches(stock)
                        var costFlag = pattern.matches(cost)
                        var unitPriceFlag = pattern.matches(unitPrice)
                        customerView!!.unitPriceField.text.toString()
                        var commodity = Commodity("",
                                customerView!!.nameField.text.toString(),
                                customerView!!.desField.text.toString(),
                                if (stockFlag) stock.toInt() else 0,
                                if (costFlag) cost.toInt() else 0,
                                if (unitPriceFlag) unitPrice.toInt() else 0,
                                fileData.fileUri.toString())
                        var insOb = presenter!!.insertItem(commodity, fileData)
                        insOb.subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe {
                                    loadItemProgressBar.visibility = View.VISIBLE
                                }
                                .doOnSuccess {
                                    Timber.d("success message " + it.message())
                                    Timber.d("success message " + it.errorBody()?.string())
                                    getItem()
                                }
                                .doOnError {
                                    Timber.d("error " + it.message)
                                }
                                .subscribe()
                    })
                    .setNegativeButton(R.string.cancel,
                            { dialog, id ->
                                dialog.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun onCreateDialog(commodity: Commodity): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            customerView = layoutInflater.inflate(R.layout.seller_fragment_commodity_dialog, null)
            customerView!!.let {
                it.nameField.setText(commodity.name)
                it.desField.setText(commodity.description)
                it.stockField.setText(commodity.stock.toString())
                it.costField.setText(commodity.cost.toString())
                it.unitPriceField.setText(commodity.unit_price.toString())
                Glide.with(activity!!).load(commodity.imageUri).into(it.commodityImageView)
            }

            ConfigUpdatePhoto.IS_UPDATE_PHOTO = false
            customerView!!.commodityImageView.setOnClickListener {
                ConfigUpdatePhoto.IS_UPDATE_PHOTO = true
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileData.fileUri)
                startActivityForResult(i, CAMERA_RESULT)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok,
                            { dialog, id ->
                                Timber.d("name " + customerView!!.nameField.text.toString())
                                var pattern = "^[0-9]*\$".toRegex()
                                var stock = customerView!!.stockField.text.toString()
                                var cost = customerView!!.costField.text.toString()
                                var unitPrice = customerView!!.unitPriceField.text.toString()
                                var stockFlag = pattern.matches(stock)
                                var costFlag = pattern.matches(cost)
                                var unitPriceFlag = pattern.matches(unitPrice)
                                customerView!!.unitPriceField.text.toString()
                                commodity.name = customerView!!.nameField.text.toString()
                                commodity.description = customerView!!.desField.text.toString()
                                commodity.stock = if (stockFlag) stock.toInt() else 0
                                commodity.cost = if (costFlag) cost.toInt() else 0
                                commodity.unit_price = if (unitPriceFlag) unitPrice.toInt() else 0
                                var updateOB = presenter!!.updateItem(commodity, fileData)
                                if (ConfigUpdatePhoto.IS_UPDATE_PHOTO) {
                                    updateOB.subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .doOnSuccess {
                                                getItem()
                                            }.doOnError {
                                            }.subscribe()
                                } else {
                                    var singleFile = NetworkManager.downloadImage(commodity.imageUri, fileData.path)
                                    singleFile.subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .doOnSuccess {
                                                updateOB.subscribeOn(Schedulers.newThread())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .doOnSuccess {
                                                            getItem()
                                                        }.doOnError {
                                                        }.subscribe()
                                            }.subscribe()
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            { dialog, id ->
                                dialog.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}