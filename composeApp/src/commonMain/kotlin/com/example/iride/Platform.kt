package com.example.iride

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform