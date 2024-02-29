package com.babestudios.companyinfouk.shared

import io.ktor.client.engine.HttpClientEngine

interface Platform {
    val name: String
    val engine: HttpClientEngine
}

expect fun getPlatform(): Platform
