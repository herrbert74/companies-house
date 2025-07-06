plugins {
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.kotlin.composeCompiler) //TODO Move to Feature plugin
}

android.namespace = "com.babestudios.companyinfouk.companies"

dependencies {
	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeFoundationLayout)
	api(libs.androidx.composeRuntime) //Transitive
	api(libs.kotlinx.coroutinesCore) //Transitive

	implementation(libs.baBeStudios.baseAndroid)
	implementation(libs.androidx.activityCompose)
	implementation(libs.androidx.composeAnimationCore) //Transitive
	implementation(libs.androidx.composeFoundation) //Transitive
	implementation(libs.androidx.composeMaterialIconsCore) //Transitive
	implementation(libs.androidx.composeRuntimeSaveable) //Transitive
	implementation(libs.androidx.constraintLayout.compose)
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)
	implementation(libs.googlePlay.servicesMaps)
	implementation(libs.googlePlay.servicesMapsCompose)

	detekt(libs.detekt.cli)
	testImplementation(libs.kotest.assertionsCore)
}
