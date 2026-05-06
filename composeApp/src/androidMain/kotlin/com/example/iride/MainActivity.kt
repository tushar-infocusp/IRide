package com.example.iride

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.iride.data.FirebaseOTPAuthManager
import com.example.iride.data.OTPAuthManager
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val activity = LocalActivity.current as Activity
            val firebaseOTPAuthManager : OTPAuthManager = koinInject()
            if (firebaseOTPAuthManager is FirebaseOTPAuthManager) {
                firebaseOTPAuthManager.setActivity(activity)
            }
            App()
        }
    }
}
