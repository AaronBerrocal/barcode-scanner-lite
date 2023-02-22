package com.bluedragoon.barcodescannerlite.presentation.ui.productview

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluedragoon.barcodescannerlite.R
import com.bluedragoon.barcodescannerlite.databinding.FragmentScannedProductBinding
import com.bluedragoon.barcodescannerlite.domain.adapters.ScannedProductsRvAdapter
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import com.bluedragoon.barcodescannerlite.presentation.ui.inventory.InventoriedProductViewModel
import com.bluedragoon.barcodescannerlite.utils.toInventoriedProduct

class ScannedProductFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ScannedProductFragment()
    }

    private val scannedProductViewModel: ScannedProductViewModel by activityViewModels()
    private val inventoriedProductViewModel: InventoriedProductViewModel by activityViewModels()

    private val scannedProductFragmentTag: String = ScannedProductFragment::class.java.name
    private var fragmentScannedProductBinding: FragmentScannedProductBinding? = null

    private var scannedProductsAdapter = ScannedProductsRvAdapter()
    private lateinit var unitsQtyEt: EditText
    private lateinit var observationsEt: EditText

    private lateinit var targetProduct: ScannableProduct

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScannedProductBinding.inflate(inflater, container, false)
        fragmentScannedProductBinding = binding

        //VIEW BINDING
        val scannedProductsRv: RecyclerView = binding.rvScannedProducts
        val lessUnitBtn = binding.ibtnLess
        unitsQtyEt = binding.etUnitsQty
        val moreUnitBtn = binding.ibtnMore
        observationsEt = binding.etDetailsValue
        val addToListBtn = binding.mbtnAddToInventoryList

        //FUNCTIONALITY
        scannedProductsRv.layoutManager = LinearLayoutManager(activity)
        scannedProductsRv.setHasFixedSize(false)
        scannedProductsRv.adapter = scannedProductsAdapter

        lessUnitBtn.setOnClickListener {
            setUnitsQty(-1)
        }

        unitsQtyEt.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus && TextUtils.isEmpty(unitsQtyEt.text.toString())){
                setUnitsQty(0)
            }
        }

        moreUnitBtn.setOnClickListener {
            setUnitsQty(1)
        }

        addToListBtn.setOnClickListener {
            //TODO: validateProductToSave()
            val productToInventory = targetProduct.toInventoriedProduct(
                unitsQtyEt.text.toString().toInt(),
                observationsEt.text.toString()
            )
            inventoriedProductViewModel.insertInventoriedProduct(productToInventory)

            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        scannedProductViewModel.getTargetProduct().observe(viewLifecycleOwner){ scannable ->
            scannable?.let { product ->
                targetProduct = product
                val scannedProductList = arrayListOf(product)
                scannedProductsAdapter.setScannedProducts(scannedProductList)
            }
        }

    }

    override fun onDestroy() {
        fragmentScannedProductBinding = null
        super.onDestroy()
    }

    private fun setUnitsQty(addedQty: Int){
        val currentUnitsQty =
            try {
                unitsQtyEt.text.toString().toInt()
            }catch (ex: NumberFormatException){
                1
            }
        unitsQtyEt.setText(
            if (currentUnitsQty + addedQty <= 1) {
                1.toString()
            } else {
                resources.getString(
                    R.string.simple_one_string,
                    currentUnitsQty + addedQty
                )
            }
        )
    }

}