package com.example.iride.viewmodel

import androidx.lifecycle.ViewModel
import com.example.iride.data.OTPAuthManager

class LoginViewModel(
    val firebaseOTPAuthManager : OTPAuthManager
) : ViewModel() {


}