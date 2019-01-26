package com.techapp.james.buybuygo.view.seller.fragment.commodity

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.*
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Commodity
import com.techapp.james.buybuygo.model.retrofitManager.RetrofitManager
import com.techapp.james.buybuygo.presenter.Configure
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import kotlinx.android.synthetic.main.fragment_commodity.*
import kotlinx.android.synthetic.main.seller_fragment_commodity_dialog.view.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import timber.log.Timber
import java.io.File
import android.app.ProgressDialog


class CommodityFragment : Fragment() {
    private var dialog: Dialog? = null
    private var customerView: View? = null
    val disposeList = ArrayList<Disposable>()
    var dataList = ArrayList<Commodity>()
    private lateinit var fileUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        var raySeller = RetrofitManager.getInstance().getRaySeller()
        var gW = raySeller.getUploadedItem("Bearer " + Configure.FB_ACESS_TOKEN)

        gW.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    loadItemProgressBar.visibility = View.GONE
                    var commodityList = it.body()?.response
                    commodityList?.let {
                        if (commodityList.size != 0) {
                            itemRecyclerView.adapter?.let {
                                (it as ListAdapter).dList = commodityList
                                it.notifyDataSetChanged()
                            }
                        }
                    }
                }
                .doOnSubscribe {
                    loadItemProgressBar.visibility = View.VISIBLE
                }
                .subscribe()
// gW cancel   resarch
        itemRecyclerView.layoutManager = GridLayoutManager(this.activity, 3, GridLayoutManager.VERTICAL, false)
//        itemRecyclerView.addItemDecoration(GItemDecoration(this.activity!!.applicationContext, R.dimen.grid_column))
        itemRecyclerView.adapter = ListAdapter(dataList, this.activity!!, this::onCreateDialog)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_RESULT -> {
                CropImage.activity(fileUri)
                        .start(this.activity!!, this)
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    var result = CropImage.getActivityResult(data!!)
                    customerView?.let {
                        it.commodityImageView.setImageURI(result.uri)
                    }
                }
            }
        }
    }

    private fun onCreateDialog(): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            customerView = layoutInflater.inflate(R.layout.seller_fragment_commodity_dialog, null)
            customerView!!.commodityImageView.setOnClickListener {
                fileUri = createImageFileUri("cacheImage")
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                startActivityForResult(i, CAMERA_RESULT)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                Timber.d("name " + customerView!!.nameField.text.toString())
                            })
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
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
                it.stockField.setText(commodity.stock)
                it.costField.setText(commodity.cost)
                it.unitPriceField.setText(commodity.unit_price)
                it.commodityImageView.setImageURI(Uri.parse(commodity.imageUri))
            }

            customerView!!.commodityImageView.setOnClickListener {
                fileUri = createImageFileUri("cacheImage")
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                startActivityForResult(i, CAMERA_RESULT)
            }
            builder.setView(customerView)
                    // Add action buttons
                    .setPositiveButton(R.string.ok,
                            DialogInterface.OnClickListener { dialog, id ->
                                Timber.d("name " + customerView!!.nameField.text.toString())
                            })
                    .setNegativeButton(R.string.cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeList
    }

    companion object {
        val CAMERA_RESULT = 0
        @JvmStatic
        fun newInstance() =
                CommodityFragment()
    }

    fun createImageFileUri(fileName: String): Uri {
        var file: File? = null
        val filePath = this.activity!!.filesDir.toString() + "/images/$fileName.jpg"
        file = File(filePath)
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir()
        }
        //share file to camera
        return FileProvider.getUriForFile(this.activity!!.applicationContext, "buybuygo.fileProvider", file!!)
    }
}
