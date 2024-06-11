plugins{
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
	alias(libs.plugins.compose.compiler) //TODO Move to Feature plugin
}

android.namespace = "com.babestudios.companyinfouk.charges"

dependencies {

	api(libs.androidx.compose.foundationLayout)
	api(libs.androidx.compose.runtime) //Transitive
	api(libs.kotlinx.coroutines.core) //Transitive

	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.constraintLayout.compose)
	implementation(libs.androidx.compose.foundation) //Transitive
	implementation(libs.decompose.extensions)
	implementation(libs.decompose.core)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.view.collapsingToolbar)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=com.arkivanov.decompose.ExperimentalDecomposeApi")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
