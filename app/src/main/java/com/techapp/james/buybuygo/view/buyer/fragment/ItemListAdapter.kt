package com.techapp.james.buybuygo.view.buyer.fragment

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class ItemListAdapter(val data: ArrayList<String>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(ImageView(context))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
    }

    class ItemViewHolder : RecyclerView.ViewHolder {
        constructor(view: View) : super(view) {
        }
    }
}