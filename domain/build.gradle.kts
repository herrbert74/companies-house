plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
}

android.namespace = "com.babestudios.companyinfouk.domain"

dependencies {

	implementation(libs.baBeStudiosBase) {
		exclude("androidx.navigation","navigation-fragment-ktx")
		exclude("androidx.navigation","navigation-ui-ktx")
	}
	implementation(libs.androidx.annotation)
	implementation(libs.google.gson)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinResult.result)
	implementation(libs.squareUp.okhttp3.okhttp)

}
