plugins {
	id("com.babestudios.companyinfouk.plugins.android")
}

@Suppress("UnstableApiUsage")
android {
	namespace = "com.babestudios.companyinfouk.common"
	buildFeatures.viewBinding = true
	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
}

dependencies {
	implementation(project(":domain"))
	implementation(libs.baBeStudiosBase)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.compose.material3)
	debugImplementation(platform(libs.google.firebase.bom))
	debugImplementation(libs.androidx.compose.ui.testManifest)

	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.constraintLayout.compose)
	implementation(libs.google.material)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensionsJetBrains)
	implementation(libs.mvikotlin.core)
	implementation(libs.mvikotlin.rx)
	implementation(libs.view.collapsingToolbar)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
}
