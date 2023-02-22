package com.bluedragoon.barcodescannerlite.presentation.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluedragoon.barcodescannerlite.data.repository.ScannableProductRepository
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarcodeScannerViewModel @Inject constructor(
    private val scannableProductRepository: ScannableProductRepository
) : ViewModel() {

    val getScannableProducts = scannableProductRepository.getScannableProducts

    fun bulkInsertScannableProducts(entities: List<ScannableProduct>): Job = viewModelScope.launch {
        scannableProductRepository.bulkInsertScannableProducts(entities)
    }

    fun deleteAllScannableProducts(): Job = viewModelScope.launch {
        scannableProductRepository.deleteAllScannableProducts()
    }

}