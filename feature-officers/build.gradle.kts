android.namespace = "com.babestudios.companyinfouk.officers"

plugins{
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.kotlin.composeCompiler) //TODO Move to Feature plugin
}

dependencies {

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeFoundationLayout)
	api(libs.androidx.composeRuntime) //Transitive
	api(libs.kotlinx.coroutinesCore) //Transitive

	implementation(libs.androidx.composeAnimationCore) //Transitive
	implementation(libs.androidx.composeFoundation) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)
	implementation(libs.androidx.activityCompose)
	implementation(libs.androidx.constraintLayout.compose)
}
