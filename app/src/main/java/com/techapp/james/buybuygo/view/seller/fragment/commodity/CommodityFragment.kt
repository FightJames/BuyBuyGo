package com.techapp.james.buybuygo.view.seller.fragment.commodity

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.Toast
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.Commodity
import com.techapp.james.buybuygo.model.file.FileData
import com.techapp.james.buybuygo.model.file.FileManager
import com.techapp.james.buybuygo.presenter.seller.CommodityPresenter
import com.techapp.james.buybuygo.view.crop.CropActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_commodity.*
import kotlinx.android.synthetic.main.seller_fragment_commodity_dialog.view.*
import timber.log.Timber


class CommodityFragment : Fragment(), com.techapp.james.buybuygo.view.View {

    private var dialog: Dialog? = null
    private lateinit var dialogHelper: DialogHelper
    var dataList = ArrayList<Commodity>()
    var presenter: CommodityPresenter? = null
    private lateinit var fileData: FileData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        fileData = FileManager.createImageFileUri("cacheImage", this.activity!!.applicationContext)
        presenter = CommodityPresenter(this)
        dialogHelper =
                DialogHelper(this, this::intentToCamera, presenter!!, this::getItem, fileData)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                dialog = dialogHelper.onCreateDialog()
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
        itemRecyclerView.layoutManager =
                GridLayoutManager(this.activity, 3, GridLayoutManager.VERTICAL, false)
        itemRecyclerView.adapter = ListAdapter(
            dataList,
            this,
            dialogHelper::onCreateModifyDialog
        )
        getItem()
    }

    fun intentToCamera() {
        ConfigUpdatePhoto.IS_UPDATE_PHOTO = true
        var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileData.fileUri)
        startActivityForResult(i, CAMERA_RESULT)
    }

    fun deleteItem(c: Commodity) {
        var deleteObserver = presenter!!.deleteItem(c)
        deleteObserver.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                it.body()?.let {
                    Toast.makeText(this.activity, it.response, Toast.LENGTH_LONG).show()
                }
                getItem()
            }
            .doOnError {
            }.subscribe()
    }

    fun pushItem(c: Commodity) {
        var singlePush = presenter!!.pushItem(c)
        singlePush.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                it.body()?.let {
                    Toast.makeText(this.activity, it.response, Toast.LENGTH_LONG).show()
                }
            }.subscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter = null
        dialogHelper.destroy()
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
                if (resultCode == RESULT_OK) {
                    var i = Intent(this.activity, CropActivity::class.java)
                    i.putExtra(CropActivity.CROP_DATA, fileData.fileUri.toString())
                    startActivityForResult(i, CropActivity.CROP_RESULT_CODE)
                } else {
                    ConfigUpdatePhoto.IS_UPDATE_PHOTO = false
                }
            }
            CropActivity.CROP_RESULT_CODE -> {
                if (resultCode == RESULT_OK) {
                    dialogHelper.customerView.let {
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
                            itemRecyclerView.adapter?.let {
                                (it as ListAdapter).dList = commodityList
                                it.notifyDataSetChanged()
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
