package com.bluedragoon.barcodescannerlite.presentation.ui.scanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bluedragoon.barcodescannerlite.R
import com.bluedragoon.barcodescannerlite.databinding.FragmentBarcodeScannerBinding
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import com.bluedragoon.barcodescannerlite.presentation.ui.productview.ScannedProductViewModel
import com.bluedragoon.barcodescannerlite.utils.ScannerState
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.button.MaterialButton

class BarcodeScannerFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = BarcodeScannerFragment()
    }

    private val barcodeScannerViewModel: BarcodeScannerViewModel by activityViewModels()
    private val scannedProductViewModel: ScannedProductViewModel by activityViewModels()

    private val barcodeScannerFragmentTag: String = BarcodeScannerFragment::class.java.name
    private var fragmentBarcodeScannerBinding : FragmentBarcodeScannerBinding? = null

    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    private lateinit var scannerStatus: TextView
    private lateinit var scanActionButton: MaterialButton

    private lateinit var scannableProductsList: ArrayList<ScannableProduct>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false)
        fragmentBarcodeScannerBinding = binding

        //VIEW BINDING
        scannerView = binding.scannerView
        scannerStatus = binding.tvScannerStatus
        scanActionButton = binding.mbtnScanningAction

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity()

        initializeCodeScanner(activity, scannerView)
        scannerStateTextHandler(ScannerState.STAND_BY)
        scannActionButtonUiHandler(ScannerState.STAND_BY)

        barcodeScannerViewModel.getScannableProducts.observe(viewLifecycleOwner){ scannables ->
            scannables?.let { products ->
            scannableProductsList = products as ArrayList<ScannableProduct>
            }
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroy() {
        fragmentBarcodeScannerBinding = null
        super.onDestroy()
    }

    private fun initializeCodeScanner(fragmentActivity: FragmentActivity, scannerView: CodeScannerView){
        codeScanner = CodeScanner(fragmentActivity, scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.PREVIEW
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback{result ->
                fragmentActivity.runOnUiThread {

                    scannerStateTextHandler(ScannerState.CODE_FOUND)
                    scannActionButtonUiHandler(ScannerState.CODE_FOUND)

                    val foundProduct = findScannableProduct(result.text)

                    if(foundProduct != null) {
                        scannedProductViewModel.setTargetProduct(foundProduct)
                        findNavController().navigate(R.id.action_nav_barcode_scanner_to_nav_scanned_product)
                    }else{
                        Log.e(barcodeScannerFragmentTag, "Producto no Encontrado")
                        Toast.makeText(requireContext(), "Producto no Encontrado", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            errorCallback = ErrorCallback { error ->
                fragmentActivity.runOnUiThread {
                    Log.e(barcodeScannerFragmentTag, "Error de inicialización: ${error.message}")
                    Toast.makeText(requireContext(), "Error de inicialización: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }

        scanActionButton.setOnClickListener {

            if(codeScanner.scanMode == ScanMode.SINGLE){
                codeScanner.scanMode = ScanMode.PREVIEW
                scannerStateTextHandler(ScannerState.STAND_BY)
                scannActionButtonUiHandler(ScannerState.STAND_BY)
            }else{
                codeScanner.scanMode = ScanMode.SINGLE
                scannerStateTextHandler(ScannerState.SCANNING)
                scannActionButtonUiHandler(ScannerState.SCANNING)
                codeScanner.startPreview()
            }

        }

    }

    private fun scannerStateTextHandler(scannerState: ScannerState){
        scannerStatus.text = scannerState.stateName
    }

    private fun scannActionButtonUiHandler(scannerState: ScannerState){
        scanActionButton.icon = when(scannerState){
            ScannerState.ENABLED,
                ScannerState.STAND_BY,
                ScannerState.CODE_FOUND -> ResourcesCompat.getDrawable(
                resources,
                R.drawable.baseline_search_24,
                requireContext().theme
            )

            ScannerState.SCANNING,
                ScannerState.DISABLED -> ResourcesCompat.getDrawable(
                resources,
                R.drawable.baseline_search_off_24,
                requireContext().theme
                )
        }

        scanActionButton.text = when(scannerState){
            ScannerState.ENABLED,
            ScannerState.STAND_BY,
            ScannerState.CODE_FOUND -> "Escanear"

            ScannerState.SCANNING,
            ScannerState.DISABLED -> "Detener"
        }

    }

    private fun findScannableProduct(sku: String) : ScannableProduct? {

        if(!::scannableProductsList.isInitialized){
            Log.e(barcodeScannerFragmentTag, "Base de datos no inizializada")
            return null
        }

        return scannableProductsList.firstOrNull { it.sku == sku }

    }

}