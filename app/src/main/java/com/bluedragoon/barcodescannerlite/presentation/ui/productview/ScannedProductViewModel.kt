package com.bluedragoon.barcodescannerlite.presentation.ui.productview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScannedProductViewModel @Inject constructor(): ViewModel(){

    private val targetProduct = MutableLiveData<ScannableProduct>()

    fun getTargetProduct(): LiveData<ScannableProduct>{
        return targetProduct
    }

    fun setTargetProduct(product: ScannableProduct){
        targetProduct.value = product
    }

}