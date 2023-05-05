plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
	kotlin("plugin.serialization").version(libs.versions.kotlin.get())
}

android.namespace = "com.babestudios.companyinfouk.domain"

dependencies {

	api(libs.baBeStudios.base.kotlin)
	api(libs.squareUp.okhttp3.okhttp)

	implementation(libs.androidx.annotation)
	implementation(libs.koin.core)
	implementation(libs.kotlin.parcelize.runtime) //Transitive
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.serialization.core) //Transitive
	implementation(libs.kotlinResult.result)

}
