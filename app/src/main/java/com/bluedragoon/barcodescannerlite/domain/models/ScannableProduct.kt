package com.bluedragoon.barcodescannerlite.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "scannable_products",
indices = [
    Index(
        value = ["sku"],
        unique = true
    )
])
data class ScannableProduct(

    @PrimaryKey(autoGenerate = true)
    val uid: Long,

    @ColumnInfo(name = "sku")
    val sku: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "pic_url")
    val picUrl: String,

    @ColumnInfo(name = "id_packaging_type")
    val idPackagingType: String,

    @ColumnInfo(name = "packaging_type")
    val packagingType: String,

    @ColumnInfo(name = "quantity_per_package")
    val quantityPerPackage: String,

)

