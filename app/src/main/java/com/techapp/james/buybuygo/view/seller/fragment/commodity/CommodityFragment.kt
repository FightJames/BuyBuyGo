package com.techapp.james.buybuygo.view.seller.fragment.commodity

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.*
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Commodity
import com.techapp.james.buybuygo.model.file.FileData
import com.techapp.james.buybuygo.model.file.FileManager
import com.techapp.james.buybuygo.presenter.seller.CommodityPresenter
import com.techapp.james.buybuygo.view.crop.CropActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commodity.*
import kotlinx.android.synthetic.main.seller_fragment_commodity_dialog.view.*
import timber.log.Timber


class CommodityFragment : Fragment() {
    private var dialog: Dialog? = null
    private var customerView: View? = null
    var dataList = ArrayList<Commodity>()
    var presenter: CommodityPresenter? = null
    private lateinit var fileData: FileData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        presenter = CommodityPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_commodity, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        val inflater = this.activity!!.menuInflater
        inflater.inflate(R.menu.seller_commodity, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.order) {
            0 -> {
                dialog = onCreateDialog()
                dialog!!.show()
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun onDetach() {
        super.onDetach()
        dialog = null
    }

    private fun init() {
        itemRecyclerView.layoutManager = GridLayoutManager(this.activity, 3, GridLayoutManager.VERTICAL, false)
        itemRecyclerView.adapter = ListAdapter(dataList, this.activity!!, this::onCreateDialog)
        getItem()
    }


    private fun onCreateDialog(): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            customerView = layoutInflater.inflate(R.layout.seller_fragment_commodity_dialog, null)
            customerView!!.commodityImageView.setOnClickListener {
                fileData = FileManager.createImageFileUri("cacheImage", this.activity!!.applicationContext)
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileData.fileUri)
                startActivityForResult(i, CAMERA_RESULT)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok, { dialog, id ->
                        Timber.d("name " + customerView!!.nameField.text.toString())
                        var commodity = Commodity("",
                                customerView!!.nameField.text.toString(),
                                customerView!!.desField.text.toString(),
                                customerView!!.stockField.text.toString().toInt(),
                                customerView!!.costField.text.toString().toInt(),
                                customerView!!.unitPriceField.text.toString().toInt(),
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

            customerView!!.commodityImageView.setOnClickListener {
                fileData = FileManager.createImageFileUri("cacheImage", this.activity!!.applicationContext)
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileData.fileUri)
                startActivityForResult(i, CAMERA_RESULT)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok,
                            { dialog, id ->
                                Timber.d("name " + customerView!!.nameField.text.toString())
                            })
                    .setNegativeButton(R.string.cancel,
                            { dialog, id ->
                                dialog.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
    }

    companion object {
        val CAMERA_RESULT = 0
        @JvmStatic
        fun newInstance() =
                CommodityFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_RESULT -> {
                var i = Intent(this.activity, CropActivity::class.java)
                i.putExtra(CropActivity.CROP_DATA, fileData.fileUri.toString())
                startActivityForResult(i, CropActivity.CROP_RESULT_CODE)

            }
            CropActivity.CROP_RESULT_CODE -> {
                if (resultCode == RESULT_OK) {
                    customerView?.let {
                        it.commodityImageView.setImageURI(fileData.fileUri)
                    }
                }
            }
        }
    }

    private fun getItem() {
        presenter!!.getUploadItem().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    loadItemProgressBar.visibility = View.GONE
                    var commodityWrapper = it.body()
                    it.body()?.let {
                        Timber.d("+++ " + it.response.size)
                        if (commodityWrapper!!.result) {
                            var commodityList = commodityWrapper.response
                            commodityList?.let {
                                if (commodityList.size != 0) {
                                    itemRecyclerView.adapter?.let {
                                        (it as ListAdapter).dList = commodityList
                                        it.notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                    }
                }
                .doOnError {
                    Timber.d("error  " + it.message)
                }
                .doOnSubscribe {
                    loadItemProgressBar.visibility = View.VISIBLE
                }
                .subscribe()
    }
}
