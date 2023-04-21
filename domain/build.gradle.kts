plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
}

android.namespace = "com.babestudios.companyinfouk.domain"

dependencies {

	api(libs.baBeStudios.base.kotlin)
	api(libs.squareUp.okhttp3.okhttp)

	implementation(libs.androidx.annotation)
	implementation(libs.google.gson)
	implementation(libs.koin.core)
	implementation(libs.kotlin.parcelize.runtime) //Transitive
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinResult.result)

}
