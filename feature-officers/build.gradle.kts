android.namespace = "com.babestudios.companyinfouk.officers"

plugins{
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.kotlin.composeCompiler) //TODO Move to Feature plugin
}

dependencies {

	api(libs.androidx.composeFoundationLayout)
	api(libs.androidx.composeRuntime) //Transitive
	api(libs.kotlinx.coroutinesCore) //Transitive

	implementation(libs.androidx.activityCompose)
	implementation(libs.androidx.composeAnimationCore) //Transitive
	implementation(libs.androidx.composeFoundation) //Transitive
	implementation(libs.androidx.constraintLayoutCompose)
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)
	implementation(libs.essenty.lifecycle)
	implementation(libs.kotlinx.datetime)
}
