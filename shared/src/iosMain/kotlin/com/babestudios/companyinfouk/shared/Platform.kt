package com.babestudios.companyinfouk.shared

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val engine: HttpClientEngine
        get() = Darwin.create()
}

actual fun getPlatform(): Platform = IOSPlatform()

actual typealias Parcelable = com.arkivanov.parcelize.darwin.Parcelable
actual typealias Parcelize = com.arkivanov.parcelize.darwin.Parcelize
