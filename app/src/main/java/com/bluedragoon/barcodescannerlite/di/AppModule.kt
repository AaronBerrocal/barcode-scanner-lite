package com.bluedragoon.barcodescannerlite.di

import android.app.Application
import androidx.room.Room
import com.bluedragoon.barcodescannerlite.data.repository.InventoriedProductRepository
import com.bluedragoon.barcodescannerlite.data.repository.ScannableProductRepository
import com.bluedragoon.barcodescannerlite.data.source.local.BarcodeScannerLiteDatabase
import com.bluedragoon.barcodescannerlite.data.source.remote.VolleySingleton
import com.bluedragoon.barcodescannerlite.utils.DATABASE_NAME
import com.bluedragoon.barcodescannerlite.utils.LocalDateTimeDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBarcodeScannerLiteDatabase(
        app: Application
    ) = Room.databaseBuilder(
        app,
        BarcodeScannerLiteDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideScannableProductRepository(db: BarcodeScannerLiteDatabase): ScannableProductRepository{
        return ScannableProductRepository(db.scannableProductDao())
    }

    @Provides
    @Singleton
    fun provideInventoriedProductRepository(db: BarcodeScannerLiteDatabase): InventoriedProductRepository {
        return InventoriedProductRepository(db.inventoriedProductDao())
    }

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder()
        .registerTypeAdapter(
            LocalDateTime::class.java,
            LocalDateTimeDeserializer()
        )
        .create()

    @Provides
    @Singleton
    fun provideVolleySingleton(app: Application): VolleySingleton {
        return VolleySingleton(app)
    }

}