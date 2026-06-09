package com.example.iride

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.iride.data.FirebaseOTPAuthManager
import com.example.iride.data.OTPAuthManager
import com.example.iride.permission.PermissionHandler
import com.example.iride.viewmodel.RideViewModel
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject
import kotlin.getValue

class MainActivity : ComponentActivity(), PermissionHandler.ActivityLauncherProvider {

    private val permissionManager: PermissionHandler by inject<PermissionHandler>()
    private var onPermissionResultCallback: ((Boolean) -> Unit)? = null
    private var onResultCallback: ((Boolean) -> Unit)? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var resultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            onPermissionResultCallback?.invoke(isGranted.all { it.value })
            onPermissionResultCallback = null
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == RESULT_OK) {
                    onResultCallback?.invoke(true)
                    onResultCallback = null
                }
            }


        checkLocationPermissions()

        setContent {
            val activity = LocalActivity.current as Activity
            val firebaseOTPAuthManager: OTPAuthManager = koinInject()
            if (firebaseOTPAuthManager is FirebaseOTPAuthManager) {
                firebaseOTPAuthManager.setActivity(activity)
            }
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        permissionManager.launcherProvider = this
    }

    override fun onStop() {
        super.onStop()
        permissionManager.launcherProvider = null
    }

    override fun launchPermissionRequest(
        manifestPermission: Array<String>,
        onResult: (Boolean) -> Unit
    ) {
        onPermissionResultCallback = onResult
        permissionLauncher.launch(manifestPermission)
    }


    override fun launchActivityResult(
        request: IntentSenderRequest, onResult: (Boolean) -> Unit
    ) {
        onResultCallback = onResult
        resultLauncher.launch(request)
    }



    private fun checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1001
            )
        }
    }
}
