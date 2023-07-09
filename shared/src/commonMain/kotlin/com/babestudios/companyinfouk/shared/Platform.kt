package com.babestudios.companyinfouk.shared

import io.ktor.client.engine.HttpClientEngine

interface Platform {
    val name: String
    val engine: HttpClientEngine
}

expect fun getPlatform(): Platform

expect interface Parcelable

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
expect annotation class Parcelize()
