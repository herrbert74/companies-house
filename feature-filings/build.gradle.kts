plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
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
	api(libs.view.collapsingToolbar)

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
