package com.example.iride.client

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun getPlatformEngine(): HttpClientEngine {
    return Darwin.create()
}