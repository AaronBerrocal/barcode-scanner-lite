package com.bluedragoon.barcodescannerlite.presentation.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluedragoon.barcodescannerlite.data.repository.InventoriedProductRepository
import com.bluedragoon.barcodescannerlite.domain.models.InventoriedProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoriedProductViewModel @Inject constructor(
    private val inventoriedProductRepository: InventoriedProductRepository
) : ViewModel() {

    val getInventoriedProducts = inventoriedProductRepository.getInventoriedProducts

    fun insertInventoriedProduct(entity: InventoriedProduct): Job = viewModelScope.launch {
        inventoriedProductRepository.insertInventoriedProduct(entity)
    }

    fun bulkInsertInventoriedProducts(entities: List<InventoriedProduct>): Job = viewModelScope.launch {
        inventoriedProductRepository.bulkInsertInventoriedProducts(entities)
    }

    fun deleteAllInventoriedProducts(): Job = viewModelScope.launch {
        inventoriedProductRepository.deleteAllInventoriedProducts()
    }

}