package com.bluedragoon.barcodescannerlite.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bluedragoon.barcodescannerlite.data.source.local.dao.InventoriedProductDao
import com.bluedragoon.barcodescannerlite.data.source.local.dao.ScannableProductDao
import com.bluedragoon.barcodescannerlite.domain.models.InventoriedProduct
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import com.bluedragoon.barcodescannerlite.utils.Converters

@Database(
    entities = [
        ScannableProduct::class,
    InventoriedProduct::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class BarcodeScannerLiteDatabase: RoomDatabase() {

    abstract fun scannableProductDao(): ScannableProductDao
    abstract fun inventoriedProductDao(): InventoriedProductDao

}