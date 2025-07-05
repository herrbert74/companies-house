plugins{
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.compose.compiler) //TODO Move to Feature plugin
}

android.namespace = "com.babestudios.companyinfouk.insolvencies"

dependencies {

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.compose.foundationLayout)
	api(libs.androidx.compose.runtime) //Transitive
	api(libs.kotlinx.coroutines.core) //Transitive

	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.compose.foundation) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)

}
