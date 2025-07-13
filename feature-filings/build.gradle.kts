plugins {
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.kotlin.composeCompiler) //TODO Move to Feature plugin
}

android.namespace = "com.babestudios.companyinfouk.filings"

dependencies {

	api(libs.androidx.activity) //Transitive
	api(libs.androidx.activityCompose)
	api(libs.androidx.composeRuntime) //Transitive
	api(libs.kotlinx.coroutinesCore) //Transitive
	api(libs.okhttp3)

	implementation(libs.androidx.constraintLayoutCompose)
	implementation(libs.androidx.composeAnimationCore) //Transitive
	implementation(libs.androidx.composeFoundation) //Transitive
	implementation(libs.androidx.composeFoundationLayout)
	implementation(libs.androidx.composeMaterialIconsCore) //Transitive
	implementation(libs.androidx.composeRuntimeSaveable) //Transitive
	implementation(libs.androidx.core) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)
	implementation(libs.uriKmp)
	implementation(libs.ktor.clientCore)

}
