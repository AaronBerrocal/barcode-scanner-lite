package com.bluedragoon.barcodescannerlite.data.repository

import androidx.lifecycle.LiveData
import com.bluedragoon.barcodescannerlite.data.source.local.dao.InventoriedProductDao
import com.bluedragoon.barcodescannerlite.domain.models.InventoriedProduct
import javax.inject.Inject

class InventoriedProductRepository @Inject constructor(
    private val inventoriedProductDao: InventoriedProductDao
) {

    val getInventoriedProducts: LiveData<List<InventoriedProduct>> = inventoriedProductDao.getInventoriedProducts()

    suspend fun insertInventoriedProduct(entity: InventoriedProduct){
        inventoriedProductDao.insert(entity)
    }

    suspend fun bulkInsertInventoriedProducts(entities: List<InventoriedProduct>){
        inventoriedProductDao.insert(entities)
    }

    suspend fun deleteAllInventoriedProducts(){
        inventoriedProductDao.deleteAllInventoriedProducts()
    }

}