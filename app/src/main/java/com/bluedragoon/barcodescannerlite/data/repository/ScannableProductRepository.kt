package com.bluedragoon.barcodescannerlite.data.repository

import androidx.lifecycle.LiveData
import com.bluedragoon.barcodescannerlite.data.source.local.dao.ScannableProductDao
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import javax.inject.Inject

class ScannableProductRepository @Inject constructor(
    private val scannableProductDao: ScannableProductDao
) {

    val getScannableProducts: LiveData<List<ScannableProduct>> = scannableProductDao.getScannableProducts()

    suspend fun bulkInsertScannableProducts(entities: List<ScannableProduct>){
        scannableProductDao.insert(entities)
    }

    suspend fun deleteAllScannableProducts(){
        scannableProductDao.deleteAllScannableProducts()
    }

}