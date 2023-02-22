package com.bluedragoon.barcodescannerlite.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct

@Dao
interface ScannableProductDao : BaseDao<ScannableProduct> {

    @Query("""SELECT * FROM scannable_products""")
    fun getScannableProducts(): LiveData<List<ScannableProduct>>

    @Query("""DELETE FROM scannable_products""")
    suspend fun deleteAllScannableProducts()

}