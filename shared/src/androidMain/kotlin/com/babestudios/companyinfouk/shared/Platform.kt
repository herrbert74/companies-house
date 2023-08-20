package com.babestudios.companyinfouk.shared

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AndroidPlatform : Platform, KoinComponent {
	override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
	override val engine: HttpClientEngine = OkHttp.create {
		addInterceptor(get())
	}
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual typealias Parcelable = android.os.Parcelable
actual typealias Parcelize = kotlinx.parcelize.Parcelize
