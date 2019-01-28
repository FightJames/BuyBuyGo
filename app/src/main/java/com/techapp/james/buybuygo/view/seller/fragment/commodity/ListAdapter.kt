package com.techapp.james.buybuygo.view.seller.fragment.commodity

import android.app.Dialog
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.data.Commodity
import kotlinx.android.synthetic.main.seller_fragment_commodity_list_item.view.*

class ListAdapter(var dList: ArrayList<Commodity>,
                  val context: Context,
                  val getDialog: ((c: Commodity) -> Dialog)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.seller_fragment_commodity_list_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dList.size

    override fun onBindViewHolder(itemViewHolder: RecyclerView.ViewHolder, position: Int) {
        itemViewHolder.itemView.nameTextView.text = dList[position].name
    }

    class ItemViewHolder : RecyclerView.ViewHolder {
        constructor(view: View) : super(view) {
            view.setOnClickListener {
            }
        }
    }
}