package com.example.iride.data

import android.widget.Toast

actual fun showToast(message: String) {
    Toast.makeText(AppContextHolder.context, message, Toast.LENGTH_SHORT).show()
}