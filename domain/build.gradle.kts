import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
}

android.namespace = "com.babestudios.companyinfouk.domain"

dependencies {

	api(Libs.Google.Dagger.core)

	implementation(Libs.baBeStudiosBase)
	implementation(Libs.AndroidX.annotation)
	implementation(Libs.Google.gson)
	implementation(Libs.Google.Dagger.Hilt.core)
	implementation(Libs.Kotlin.Coroutines.core)
	implementation(Libs.KotlinResult.result)
	implementation(Libs.SquareUp.OkHttp3.okHttp)

	kapt(Libs.Google.Dagger.compiler)
	kapt(Libs.Google.Dagger.Hilt.compiler)
}
