plugins {
	id("android-library-convention")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
		freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
	}

	sourceSets.getByName("commonMain").dependencies {
		api(project(":shared"))
		implementation(project(":common"))

		implementation(project.dependencies.platform(libs.androidx.composeBom))

		implementation(libs.androidx.composeFoundation)
		implementation(libs.androidx.composeFoundationLayout)
		implementation(libs.androidx.composeUi)
		implementation(libs.androidx.composeUiGraphics)
		implementation(libs.androidx.composeUiText)
		implementation(libs.androidx.composeUiUnit)
		implementation(libs.androidx.composeUiTooling)
		implementation(libs.androidx.composeUiToolingPreview)
		implementation(libs.androidx.composeMaterial3)
		api(libs.androidx.composeRuntime)
		implementation(libs.kotlinx.collectionsImmutableJvm)

		implementation(libs.baBeStudios.baseCompose)

		api(libs.kotlinx.coroutinesCore)
	}

	// sourceSets.getByName("commonTest").dependencies {
	// 	implementation(libs.kotlinx.coroutinesTest)
	// }
}
