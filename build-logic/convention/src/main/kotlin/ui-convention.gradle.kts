plugins {
	alias(libs.plugins.androidLibrary)
	//id("com.android.library")
	kotlin("android")
}

android {
	buildFeatures {
		compose = true
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
		freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
	}
}

dependencies {
	api(project(":shared"))
	implementation(project(":common"))

	implementation(platform(libs.androidx.composeBom))

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

	implementation(libs.baBeStudios.baseCompose)

	api(libs.kotlinx.coroutinesCore)

	testImplementation(libs.kotlinx.coroutinesTest)
}
