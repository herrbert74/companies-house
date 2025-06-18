plugins {
	alias(libs.plugins.compose.compiler) //TODO Move to Feature plugin
	id("android-library-convention")
}

android {
	namespace = "com.babestudios.companyinfouk.common"
	buildFeatures.compose = true
}

dependencies {
	api(project(":shared"))

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.activity.activity) //Transitive
	api(libs.androidx.compose.material3)
	api(libs.androidx.compose.ui.ui) //Transitive
	api(libs.androidx.compose.ui.text) //Transitive
	api(libs.androidx.compose.runtime) //Transitive
	api(libs.androidx.compose.foundationLayout) //Transitive
	api(libs.androidx.compose.foundationLayout.android) //Transitive
	api(libs.view.collapsingToolbar)

//An exclude example
//	api(
//		libs.androidx.compose.material3.get().let { "${it.module}:${it.versionConstraint.requiredVersion}" }
//	) {
//		exclude("androidx.compose.material3", "material3-android")
//	}

	implementation(libs.baBeStudios.base.android)

	debugImplementation(platform(libs.google.firebase.bom))

	implementation(libs.androidx.annotation) //Transitive
	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.compose.foundation) //Transitive
	implementation(libs.androidx.compose.material.icons.core) //Transitive
	implementation(libs.androidx.compose.ui.graphics) //Transitive
	implementation(libs.androidx.compose.ui.toolingPreview) //Transitive
	implementation(libs.androidx.compose.ui.unit) //Transitive
	implementation(libs.androidx.constraintLayout.compose)
	implementation(libs.kotlinx.coroutines.core) //Transitive

	runtimeOnly(libs.androidx.compose.ui.tooling)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3ExpressiveApi")
}
