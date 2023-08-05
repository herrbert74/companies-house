plugins {
	id("com.babestudios.companyinfouk.plugins.android")
}

android {
	namespace = "com.babestudios.companyinfouk.common"
	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
}

dependencies {
	api(project(":shared"))

	implementation(platform(libs.androidx.compose.bom))
	api(libs.androidx.compose.material3)
	api(libs.androidx.compose.ui) //Transitive
	api(libs.androidx.compose.ui.text) //Transitive
	api(libs.androidx.compose.runtime) //Transitive
	api(libs.androidx.compose.foundationLayout) //Transitive
	api(libs.androidx.appcompat)
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
	implementation(libs.androidx.compose.foundation) //Transitive
	implementation(libs.androidx.compose.material.icons.core) //Transitive
	implementation(libs.androidx.compose.ui.graphics) //Transitive
	implementation(libs.androidx.compose.ui.toolingPreview) //Transitive
	implementation(libs.androidx.compose.ui.unit) //Transitive
	implementation(libs.androidx.constraintLayout.compose)
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensionsJetBrains)
	implementation(libs.mvikotlin.core)
	implementation(libs.mvikotlin.rx)

	runtimeOnly(libs.androidx.compose.ui.tooling)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
}
