package com.bluedragoon.barcodescannerlite.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bluedragoon.barcodescannerlite.R
import com.bluedragoon.barcodescannerlite.databinding.ProductInfoCardItemBinding
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import com.squareup.picasso.Picasso

class ScannedProductsRvAdapter() : RecyclerView.Adapter<ScannedProductsRvAdapter.ScannedProductsViewHolder>() {

    private var scannedProductsList = listOf<ScannableProduct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.product_info_card_item,
            parent,
            false
        )

        return ScannedProductsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScannedProductsViewHolder, position: Int) {
        val currentItem = scannedProductsList[position]

        holder.skuTv.text = currentItem.sku
        holder.nameTv.text = currentItem.name
        holder.packageTv.text = currentItem.packagingType
        holder.qtyxpckgTv.text = currentItem.quantityPerPackage

        Picasso.get().load(currentItem.picUrl).fit().centerCrop()
            .placeholder(R.drawable.baseline_image_24)
            .error(R.drawable.baseline_broken_image_24)
            .into(holder.picIv)

    }

    override fun getItemCount(): Int = scannedProductsList.size

    fun setScannedProducts(scannedProductsList: List<ScannableProduct>){
        this.scannedProductsList = scannedProductsList
        notifyDataSetChanged()
    }

    inner class ScannedProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val binding = ProductInfoCardItemBinding.bind(itemView)

        val picIv: ImageView = binding.ivProductPic
        val skuTv: TextView = binding.tvSkuValue
        val nameTv: TextView = binding.tvProductValue
        val packageTv: TextView = binding.tvPackageValue
        val qtyxpckgTv: TextView = binding.tvQtyxpckgValue

    }

}