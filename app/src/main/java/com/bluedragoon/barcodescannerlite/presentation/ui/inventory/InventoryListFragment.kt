package com.bluedragoon.barcodescannerlite.presentation.ui.inventory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluedragoon.barcodescannerlite.databinding.FragmentInventoryListBinding
import com.bluedragoon.barcodescannerlite.domain.adapters.InventoriedProductsRvAdapter
import com.bluedragoon.barcodescannerlite.domain.models.InventoriedProduct
import com.google.android.material.button.MaterialButton
import com.opencsv.CSVWriter
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Objects


class InventoryListFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = InventoryListFragment()
    }

    private val inventoriedProductViewModel: InventoriedProductViewModel by activityViewModels()

    private val inventoryListFragmentTag: String = InventoryListFragment::class.java.name
    private var fragmentInventoryListBinding : FragmentInventoryListBinding? = null

    private var inventoriedProductsAdapter = InventoriedProductsRvAdapter()
    private lateinit var inventoriedProductsList: ArrayList<InventoriedProduct>

    private lateinit var skuQtyTotal: TextView
    private lateinit var unitsQtyTotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInventoryListBinding.inflate(inflater, container, false)
        fragmentInventoryListBinding = binding

        //VIEW BINDING
        val inventoriedProductsRv : RecyclerView = binding.rvInventoriedProducts
        skuQtyTotal = binding.tvSkuTotalsValue
        unitsQtyTotal = binding.tvUnitsTotalsValue

        val deleteInventoriedProductsListBtn : MaterialButton = binding.mbtnDeleteInventoryList
        val createInventoriedProductsListBtn : MaterialButton = binding.mbtnInventoryListToCsv

        //FUNCTIONALITY
        inventoriedProductsRv.layoutManager = LinearLayoutManager(activity)
        inventoriedProductsRv.setHasFixedSize(false)
        inventoriedProductsRv.adapter = inventoriedProductsAdapter

        deleteInventoriedProductsListBtn.setOnClickListener {
            //TODO: SHOW ALERT DIALOG
            inventoriedProductViewModel.deleteAllInventoriedProducts()
        }

        createInventoriedProductsListBtn.setOnClickListener {
//            openDirectory()
            val ts = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
//            val path = Environment.getExternalStorageDirectory().absolutePath + "/BCSLite/Inventario_${ts}.csv"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path + "/Inventario_${ts}.csv"
//            val path = "${activity?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}/Inventario_${ts}.csv"
            csvWriterFile(path, inventoriedProductsList)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        inventoriedProductViewModel.getInventoriedProducts.observe(viewLifecycleOwner){ inventoried ->
            inventoried?.let { products ->
                inventoriedProductsList = products as ArrayList<InventoriedProduct>
                inventoriedProductsAdapter.setInventoriedProducts(products)
                skuQtyTotal.text = String.format("%d", products.size)
                unitsQtyTotal.text = if(products.isNotEmpty()){
                    String.format("%d", products.sumOf { it.units })
                }else{
                    0.toString()
                }
            }
        }

    }

    override fun onDestroy() {
        fragmentInventoryListBinding = null
        super.onDestroy()
    }

    private fun csvWriterFile(path: String, dataList: ArrayList<InventoriedProduct>){
//        var writer: CSVWriter? = null
        try {

            val writer = CSVWriter(FileWriter(path))
            val data: MutableList<Array<String>> = ArrayList()
            data.add(arrayOf(
                "sku",
                "nombre",
                "id_tipo_empaque",
                "tipo_empaque",
                "cantidad_por_empaque",
                "unidades_inventariadas",
                "observaciones"
            ))

            dataList.forEach{ product ->
                data.add(
                    arrayOf(
                        product.sku,
                        product.name,
                        product.idPackagingType,
                        product.packagingType,
                        product.quantityPerPackage,
                        product.units.toString(),
                        product.details
                    )
                )
            }

            writer.writeAll(data)
            writer.close()
            Toast.makeText(requireContext(), "Archivo generado con éxito", Toast.LENGTH_SHORT).show()
//            callRead()
        } catch (e: IOException) {
            Log.e(inventoryListFragmentTag, e.printStackTrace().toString())
            Toast.makeText(requireContext(), "Error al generar archivo", Toast.LENGTH_SHORT).show()
        }
    }

    var baseDocumentTreeUri: Uri? = null

    fun openDirectory(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

        getResult.launch(intent)
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
            if(activityResult.resultCode == Activity.RESULT_OK){
//                val value = activityResult.data?.getStringExtra("input")
                baseDocumentTreeUri = Objects.requireNonNull(activityResult.data)?.data

//                val takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION && Intent.FLAG_GR)

                requireContext().contentResolver.takePersistableUriPermission(activityResult.data!!.data!!, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                val ts = LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
                val path = "$baseDocumentTreeUri/Inventario_${ts}.csv"

                csvWriterFile(path, inventoriedProductsList)
            }else{
                Log.e(inventoryListFragmentTag, "Error for activity result $activityResult")
            }
        }

}