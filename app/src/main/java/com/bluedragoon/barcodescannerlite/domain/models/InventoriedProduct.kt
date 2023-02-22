package com.bluedragoon.barcodescannerlite.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "inventoried_products",
    indices = [
        Index(
            value = ["sku"],
            unique = true
        )
    ] )
data class InventoriedProduct(

    @PrimaryKey(autoGenerate = true)
    val uid: Long,

    @ColumnInfo(name = "sku")
    val sku: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "id_packaging_type")
    val idPackagingType: String,

    @ColumnInfo(name = "packaging_type")
    val packagingType: String,

    @ColumnInfo(name = "quantity_per_package")
    val quantityPerPackage: String,

    @ColumnInfo(name = "units")
    val units: Int,

    @ColumnInfo(name = "details")
    val details: String

){
}
