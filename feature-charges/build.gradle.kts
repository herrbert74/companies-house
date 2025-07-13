plugins{
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.kotlin.composeCompiler) //TODO Move to Feature plugin
}

android.namespace = "com.babestudios.companyinfouk.charges"

dependencies {

	api(libs.androidx.composeFoundationLayout)
	api(libs.androidx.composeRuntime) //Transitive
	api(libs.kotlinx.coroutinesCore) //Transitive

	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.activityCompose)
	implementation(libs.androidx.composeAnimationCore) //Transitive
	implementation(libs.androidx.constraintLayoutCompose)
	implementation(libs.androidx.composeFoundation) //Transitive
	implementation(libs.decompose.extensions)
	implementation(libs.decompose.core)
}
