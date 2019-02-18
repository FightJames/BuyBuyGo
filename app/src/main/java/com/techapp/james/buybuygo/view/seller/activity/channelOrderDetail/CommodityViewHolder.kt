package com.techapp.james.buybuygo.view.seller.activity.channelOrderDetail

import android.support.v7.widget.RecyclerView
import android.view.View
import com.techapp.james.buybuygo.model.data.seller.CommodityRecord
import kotlinx.android.synthetic.main.seller_activity_channel_order_detail_commodity_record_list_item.view.*

class CommodityViewHolder : RecyclerView.ViewHolder {
    constructor(view: View) : super(view)

    fun setData(commodityRecord: CommodityRecord) {
        itemView.nameLabel.text =
                String.format(itemView.nameLabel.text.toString(), commodityRecord.name)
        itemView.descriptionLabel.text =
                String.format(
                    itemView.descriptionLabel.text.toString(),
                    commodityRecord.description
                )
        itemView.costLabel.text =
                String.format(itemView.costLabel.text.toString(), commodityRecord.cost)
        itemView.unitPriceLabel.text =
                String.format(itemView.unitPriceLabel.text.toString(), commodityRecord.unitPrice)
        itemView.profitLabel.text =
                String.format(itemView.profitLabel.text.toString(), commodityRecord.profit)
        itemView.totalCostLabel.text =
                String.format(itemView.totalCostLabel.text.toString(), commodityRecord.totalCost)
        itemView.quantityLabel.text =
                String.format(itemView.quantityLabel.text.toString(), commodityRecord.quantity)
        itemView.turnoverLabel.text =
                String.format(itemView.turnoverLabel.text.toString(), commodityRecord.turnover)
    }
}