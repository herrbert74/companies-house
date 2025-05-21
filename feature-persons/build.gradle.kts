plugins {
	id("android-library-convention")
	id("ui-convention")
	alias(libs.plugins.compose.compiler) //TODO Move to Feature plugin
}

android.namespace = "com.babestudios.companyinfouk.persons"

dependencies {

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.compose.foundationLayout)
	api(libs.androidx.compose.runtime) //Transitive
	api(libs.kotlinx.coroutines.core) //Transitive

	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.compose.foundation) //Transitive
	implementation(libs.baBeStudios.base.android)
	implementation(libs.baBeStudios.base.compose)
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)
	implementation(libs.androidx.activity.compose)
	implementation(libs.view.collapsingToolbar)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=com.arkivanov.decompose.ExperimentalDecomposeApi")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
