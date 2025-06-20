import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins{
	id("android-library-convention")
	id("ui-convention")
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
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=com.arkivanov.decompose.ExperimentalDecomposeApi")
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
