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
    var dList: ArrayList<Commodity>,
    val commodityFragment: CommodityFragment,
    val getDialog: ((c: Commodity) -> Dialog)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView = LayoutInflater.from(commodityFragment.activity)
            .inflate(R.layout.seller_fragment_commodity_list_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dList.size

    override fun onBindViewHolder(itemViewHolder: RecyclerView.ViewHolder, position: Int) {
        itemViewHolder.itemView.nameTextView.text = dList[position].name
        Timber.d("image url " + dList[position].imageUrl)
        Glide.with(commodityFragment).load(dList[position].imageUrl)
            .into(itemViewHolder.itemView.commodityImageView)
        itemViewHolder.itemView.commodityImageView.setOnClickListener {
            getDialog.invoke(dList[position]).show()
        }
        itemViewHolder.itemView.deleteImageView.setOnClickListener {
            commodityFragment.deleteItem(dList[position])
        }
        itemViewHolder.itemView.pushBtn.setOnClickListener {
            commodityFragment.pushItem(dList[position])
        }
    }

    class ItemViewHolder : RecyclerView.ViewHolder {
        constructor(view: View) : super(view) {
        }
    }
}