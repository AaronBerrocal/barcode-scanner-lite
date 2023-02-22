package com.bluedragoon.barcodescannerlite.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bluedragoon.barcodescannerlite.R
import com.bluedragoon.barcodescannerlite.databinding.InventoriedProductInfoCardItemBinding
import com.bluedragoon.barcodescannerlite.domain.models.InventoriedProduct

class InventoriedProductsRvAdapter() : RecyclerView.Adapter<InventoriedProductsRvAdapter.InventoriedProductsViewHolder>() {

    private var inventoriedProductsList = listOf<InventoriedProduct>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoriedProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.inventoried_product_info_card_item,
            parent,
            false
        )

        return InventoriedProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InventoriedProductsViewHolder, position: Int) {
        val currentItem = inventoriedProductsList[position]

        holder.indexTv.text = holder.itemView.context.getString(R.string.simple_one_string, position + 1)
        holder.skuTv.text = currentItem.sku
        holder.nameTv.text = currentItem.name
        holder.packageTv.text = currentItem.packagingType
        holder.qtyPerPackageTv.text = currentItem.quantityPerPackage
        holder.unitsTv.text = String.format("%d", currentItem.units)

    }

    override fun getItemCount(): Int = inventoriedProductsList.size

    fun setInventoriedProducts(inventoriedProductsList: List<InventoriedProduct>){
        this.inventoriedProductsList = inventoriedProductsList
        notifyDataSetChanged()
    }

    inner class InventoriedProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val binding = InventoriedProductInfoCardItemBinding.bind(itemView)

        val indexTv: TextView = binding.tvInvIdx
        val skuTv: TextView = binding.tvInvSku
        val nameTv: TextView = binding.tvInvName
        val packageTv: TextView = binding.tvInvPackageType
        val qtyPerPackageTv: TextView = binding.tvInvQtyppkg
        val unitsTv: TextView = binding.tvInvUnitsValue

    }



}