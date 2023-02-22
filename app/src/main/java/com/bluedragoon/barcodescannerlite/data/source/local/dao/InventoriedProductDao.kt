package com.bluedragoon.barcodescannerlite.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bluedragoon.barcodescannerlite.domain.models.InventoriedProduct

@Dao
interface InventoriedProductDao : BaseDao<InventoriedProduct> {

    @Query("""SELECT * FROM inventoried_products""")
    fun getInventoriedProducts(): LiveData<List<InventoriedProduct>>

    @Query("""DELETE FROM inventoried_products""")
    suspend fun deleteAllInventoriedProducts()

}