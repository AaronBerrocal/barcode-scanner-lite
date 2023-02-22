package com.bluedragoon.barcodescannerlite.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.bluedragoon.barcodescannerlite.domain.models.InventoriedProduct
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun ScannableProduct.toInventoriedProduct(units: Int, details: String): InventoriedProduct{
    return InventoriedProduct(
        0,
        this.sku,
        this.name,
        this.idPackagingType,
        this.packagingType,
        this.quantityPerPackage,
        units,
        details
    )
}