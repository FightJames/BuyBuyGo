package com.techapp.james.buybuygo.view.seller.fragment.commodity

import android.app.Dialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.seller.Commodity
import kotlinx.android.synthetic.main.seller_fragment_commodity_list_item.view.*
import timber.log.Timber

class ListAdapter(
    var dataList: ArrayList<Commodity>,
    var operationListener: OperationListener? = null,
    val getDialog: ((c: Commodity) -> Dialog)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OperationListener {
        fun deleteItem(c: Commodity)
        fun pushItem(c: Commodity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.seller_fragment_commodity_list_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(itemViewHolder: RecyclerView.ViewHolder, position: Int) {
        itemViewHolder.itemView.nameTextView.text = dataList[position].name
        Timber.d("image liveUrl " + dataList[position].imageUrl)
        Glide.with(itemViewHolder.itemView.context).load(dataList[position].imageUrl)
            .into(itemViewHolder.itemView.commodityImageView)
        itemViewHolder.itemView.commodityImageView.setOnClickListener {
            getDialog.invoke(dataList[position]).show()
        }
        itemViewHolder.itemView.deleteImageView.setOnClickListener {
            operationListener?.deleteItem(dataList[position])
        }
        itemViewHolder.itemView.pushBtn.setOnClickListener {
            operationListener?.pushItem(dataList[position])
        }
    }

    class ItemViewHolder : RecyclerView.ViewHolder {
        constructor(view: View) : super(view) {
        }
    }
}