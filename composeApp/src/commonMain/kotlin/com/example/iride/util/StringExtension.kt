package com.example.iride.util

fun String.isValidEmail(): Boolean {
    if (this.isBlank()) return false

    val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    return emailRegex.matches(this)
}