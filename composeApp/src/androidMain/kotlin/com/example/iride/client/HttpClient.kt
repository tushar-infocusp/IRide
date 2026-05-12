package com.example.iride.client

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun getPlatformEngine(): HttpClientEngine {
    return OkHttp.create()
}