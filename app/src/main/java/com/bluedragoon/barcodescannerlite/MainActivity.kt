package com.bluedragoon.barcodescannerlite

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bluedragoon.barcodescannerlite.data.source.remote.RestRequestsManager
import com.bluedragoon.barcodescannerlite.data.source.remote.VolleyRestCallbacks
import com.bluedragoon.barcodescannerlite.data.source.remote.VolleySingleton
import com.bluedragoon.barcodescannerlite.databinding.ActivityMainBinding
import com.bluedragoon.barcodescannerlite.domain.models.ScannableProduct
import com.bluedragoon.barcodescannerlite.presentation.ui.scanner.BarcodeScannerViewModel
import com.bluedragoon.barcodescannerlite.utils.REQUEST_SUCCESSFUL_VALUE
import com.bluedragoon.barcodescannerlite.utils.RestResponseProperties
import com.bluedragoon.barcodescannerlite.utils.SCANNABLE_PRODUCTS_CATALOG_MOCK_DATA_URL
import com.bluedragoon.barcodescannerlite.utils.SCANNABLE_PRODUCTS_CATALOG_VOLLEY_TAG
import com.google.gson.Gson
import com.google.gson.JsonParseException
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var gson: Gson
    @Inject lateinit var volleySingleton: VolleySingleton
    @Inject lateinit var restRequestsManager: RestRequestsManager

    val viewModel: BarcodeScannerViewModel by viewModels()

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        //        val navMenuBadge: ImageView = navView.getHeaderView(0).findViewById(R.id.iv_nav_drawer_badge)
        val navHeaderTitle: TextView =
            navView.getHeaderView(0).findViewById(R.id.tv_nav_drawer_title)
        val navHeaderSubtitle: TextView =
            navView.getHeaderView(0).findViewById(R.id.tv_nav_drawer_subtitle)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_barcode_scanner,
                R.id.nav_inventory_list
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()

        restRequestsManager.executeGetRequest(
            SCANNABLE_PRODUCTS_CATALOG_MOCK_DATA_URL,
            SCANNABLE_PRODUCTS_CATALOG_VOLLEY_TAG,
            volleySingleton,
            object : VolleyRestCallbacks{
                override fun onSuccess(response: JSONObject?, tag: String) {
                    if(tag == SCANNABLE_PRODUCTS_CATALOG_VOLLEY_TAG){
                        if(response?.getString(RestResponseProperties.STATUS.keyName) == REQUEST_SUCCESSFUL_VALUE){
                            val dataJsonArray = response.getJSONArray(
                                RestResponseProperties.RECORDS.keyName
                            )
                            val dataArrayList: ArrayList<ScannableProduct> =
                                try {
                                    gson
                                        .fromJson(
                                            dataJsonArray.toString(),
                                            Array<ScannableProduct>::class.java
                                        )
                                        .toCollection(ArrayList())
                                }catch (e: JsonParseException){
                                    Log.e("Gson Parse Error: ", e.printStackTrace().toString())
                                    arrayListOf()
                                }
                            if(dataArrayList.isNotEmpty()){
                                viewModel.deleteAllScannableProducts()
                                viewModel.bulkInsertScannableProducts(dataArrayList)
                            }
                        }
                    }
                }

                override fun onError(error: String, tag: String) {
                    if(tag == SCANNABLE_PRODUCTS_CATALOG_VOLLEY_TAG){
                        Log.e("Request Error: ", error)
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}