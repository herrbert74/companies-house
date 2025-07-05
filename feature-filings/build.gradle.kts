plugins {
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.compose.compiler) //TODO Move to Feature plugin
}

android.namespace = "com.babestudios.companyinfouk.filings"

dependencies {

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.activity.activity) //Transitive
	api(libs.androidx.activity.compose)
	api(libs.androidx.compose.runtime) //Transitive
	api(libs.kotlinx.coroutines.core) //Transitive
	api(libs.squareUp.okhttp3.okhttp)

	implementation(libs.androidx.constraintLayout.compose)
	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.compose.foundation) //Transitive
	implementation(libs.androidx.compose.foundationLayout)
	implementation(libs.androidx.compose.material.icons.core) //Transitive
	implementation(libs.androidx.compose.runtime.saveable) //Transitive
	implementation(libs.androidx.core) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)
	implementation(libs.uriKmp)
	implementation(libs.ktor.client.core)

}
