plugins {
	alias(libs.plugins.kotlin.composeCompiler) //TODO Move to Feature plugin
	id("android-library-convention")
}

android {
	namespace = "com.babestudios.companyinfouk.common"
	buildFeatures.compose = true
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll(
			listOf(
				"-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api",
				"-Xopt-in=androidx.compose.material3.ExperimentalMaterial3ExpressiveApi"
			)
		)
	}
}

dependencies {
	api(project(":shared"))

	implementation(platform(libs.androidx.composeBom))

	api(libs.androidx.activity) //Transitive
	api(libs.androidx.composeMaterial3)
	api(libs.androidx.composeUi) //Transitive
	api(libs.androidx.composeUiText) //Transitive
	api(libs.androidx.composeRuntime) //Transitive
	api(libs.androidx.composeFoundationLayout) //Transitive
	api(libs.androidx.composeFoundationLayoutAndroid) //Transitive

//An exclude example
//	api(
//		libs.androidx.compose.material3.get().let { "${it.module}:${it.versionConstraint.requiredVersion}" }
//	) {
//		exclude("androidx.compose.material3", "material3-android")
//	}

	implementation(libs.baBeStudios.baseAndroid)

	debugImplementation(platform(libs.firebaseBom))

	implementation(libs.androidx.annotation) //Transitive
	implementation(libs.androidx.composeAnimationCore) //Transitive
	implementation(libs.androidx.composeFoundation) //Transitive
	implementation(libs.androidx.composeMaterialIconsCore) //Transitive
	implementation(libs.androidx.composeUiGraphics) //Transitive
	implementation(libs.androidx.composeUiToolingPreview) //Transitive
	implementation(libs.androidx.composeUiUnit) //Transitive
	implementation(libs.androidx.constraintLayoutCompose)
	implementation(libs.kotlinx.coroutinesCore) //Transitive

	runtimeOnly(libs.androidx.composeUiTooling)
}
