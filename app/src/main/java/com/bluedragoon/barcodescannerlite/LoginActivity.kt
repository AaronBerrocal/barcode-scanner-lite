package com.bluedragoon.barcodescannerlite

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewbinding.BuildConfig
import com.bluedragoon.barcodescannerlite.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private val loginActivityTag: String = LoginActivity::class.java.simpleName

    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginIntent: Intent

    private lateinit var layout: View

    private val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener {
            true
        }

        layout = binding.root

        loginIntent = Intent(this, MainActivity::class.java)

        val pkgInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        }else{
            packageManager.getPackageInfo(packageName, 0)
        }

        //View Binding
        val versionNameTv: TextView = binding.tvVersionName
        val userTil: TextInputLayout = binding.tilUser
        val userTiet: TextInputEditText = binding.tietUserCredential
        val passwordTil: TextInputLayout = binding.tilPassword
        val passwordTiet: TextInputEditText = binding.tietPasswordCredential
        val loginMbtn: Button = binding.btnLoginExecute

        //Functionality
        versionNameTv.text = getString(R.string.app_version_name, pkgInfo.versionName)

        passwordTiet.setOnEditorActionListener { _, actionId, event ->
            if ((event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                loginMbtn.performClick()
            }
            false
        }

        loginMbtn.setOnClickListener {

            val typedUser: String = userTiet.text.toString().trim()
            val typedPassword: String = passwordTiet.text.toString().trim()

            val isUserValid = validateDataString(typedUser, userTil)
            val isPasswordValid = validateDataString(typedPassword, passwordTil)

            if (isUserValid && isPasswordValid) {
                if(typedUser == "user" && typedPassword == "user"){
                    if (checkMultiplePermissions(this, requiredPermissions)) {
                        requestMultiplePermissions(requiredPermissions)
                    } else {
                        startActivity(loginIntent)
                        finish()
                    }
                }else{
                    val errorStr = "Credenciales Inválidas"
                    toggleTilError(userTil, errorStr)
                    userTiet.text?.clear()
                    passwordTiet.text?.clear()
                }
            }else {
                userTiet.text?.clear()
                passwordTiet.text?.clear()
            }
        }

    }

    private fun validateDataString(
        uStr: String,
        uTil: TextInputLayout
    ): Boolean {
        var uErrorMsg = ""
        var validation = false

        if (uStr.isBlank()) {
            uErrorMsg = "Campo Requerido."
        } else {
            validation = true
        }
        toggleTilError(uTil, uErrorMsg)

        return validation
    }

    private fun toggleTilError(til: TextInputLayout, msg: String) {
        til.error = msg
        til.isErrorEnabled = msg.isNotEmpty()
    }

    //region ****  PERMISSIONS HANDLING  ****

    private fun checkMultiplePermissions(context: Context, permissions: Array<String>): Boolean =
        !permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun requestMultiplePermissions(permissions: Array<String>) {
        if (permissions.isNotEmpty()) {
            if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
                Log.i(
                    loginActivityTag,
                    "Displaying permission rationale to provide additional context."
                )
                Snackbar.make(
                    binding.root,
                    R.string.permission_required,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.ok) {
                        requestMultiplePermissionsLanuncher.launch(permissions)
                    }
                    .show()
            } else {
                requestMultiplePermissionsLanuncher.launch(permissions)
            }
        }
    }

    private val requestMultiplePermissionsLanuncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.any { !it.value }) {
            //Permission denied.
            Snackbar.make(
                layout,
                R.string.permissions_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        BuildConfig.LIBRARY_PACKAGE_NAME, null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .show()
        } else {
            startActivity(loginIntent)
            finish()
        }
    }

    //endregion ****  PERMISSIONS HANDLING  ****
}