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


class CommodityFragment : Fragment(), CommodityView, ListAdapter.OperationListener,
    DialogHelper.CreateCallback, DialogHelper.ModifyCallBack {

    private var dialog: Dialog? = null
    private lateinit var dialogHelper: DialogHelper
    lateinit var presenter: CommodityPresenter
    private lateinit var fileData: FileData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        fileData = FileManager.createImageFileUri("cacheImage", this.activity!!.applicationContext)
        presenter = CommodityPresenter(this)
        dialogHelper =
                DialogHelper(this, this::intentToCamera, fileData)
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
                dialog = dialogHelper.createDialog(this)
                dialog!!.show()
            }
        }
        return true
    }

    override fun onStart() {
        super.onStart()
    }

    private fun init() {
        itemRecyclerView.layoutManager =
                GridLayoutManager(this.activity, 3, GridLayoutManager.VERTICAL, false)
        itemRecyclerView.adapter = ListAdapter(
            ArrayList<Commodity>(),
            this
        )
        presenter.getUploadItem()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    fun intentToCamera() {
        ConfigUpdatePhoto.IS_UPDATE_PHOTO = true
        var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileData.fileUri)
        startActivityForResult(i, CAMERA_RESULT)
    }

    override fun showRequestMessage(s: String) {
        Toast.makeText(this.activity, s, Toast.LENGTH_LONG).show()
    }

    override fun isLoad(flag: Boolean) {
        if (flag) {
            loadItemProgressBar.visibility = View.VISIBLE
            itemRecyclerView.visibility = View.INVISIBLE
        } else {
            loadItemProgressBar.visibility = View.INVISIBLE
            itemRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun updateCommodityList(list: ArrayList<Commodity>) {
        itemRecyclerView?.adapter?.let {
            (it as ListAdapter).dataList = list
            it.notifyDataSetChanged()
        }
    }

    // list adapter callback
    override fun deleteItem(c: Commodity) {
        presenter!!.deleteItem(c)
    }

    override fun pushItem(c: Commodity) {
        presenter!!.pushItem(c)
    }

    override fun modifyItem(c: Commodity) {
        dialogHelper.createModifyDialog(c, this).show()
    }


    //Dialog callback
    override fun onCreate(c: Commodity, fileData: FileData) {
        presenter!!.insertItem(c, fileData)

    }

    override fun onModify(c: Commodity, fileData: FileData) {
        presenter.updateItem(c, fileData)
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
}
