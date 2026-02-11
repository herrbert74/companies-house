import com.babestudios.companyinfouk.androidLibrary

plugins {
	alias(libs.plugins.kotlin.composeCompiler) // TODO Move to Feature plugin
	id("android-library-convention")
}

androidLibrary {
	namespace = "com.babestudios.companyinfouk.common"
	// buildFeatures.compose = true // In KMP AGP this might be different
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
	sourceSets.getByName("commonMain").dependencies {
		api(project(":shared"))

		implementation(project.dependencies.platform(libs.androidx.composeBom))

		api(libs.androidx.activity) // Transitive
		api(libs.androidx.composeMaterial3)
		api(libs.androidx.composeUi) // Transitive
		api(libs.androidx.composeUiText) // Transitive
		api(libs.androidx.composeRuntime) // Transitive
		api(libs.androidx.composeFoundationLayout) // Transitive
		api(libs.androidx.composeFoundationLayoutAndroid) // Transitive

		implementation(libs.baBeStudios.baseAndroid)

		implementation(libs.androidx.annotation) // Transitive
		implementation(libs.androidx.composeAnimationCore) // Transitive
		implementation(libs.androidx.composeFoundation) // Transitive
		implementation(libs.androidx.composeMaterialIconsCore) // Transitive
		implementation(libs.androidx.composeUiGraphics) // Transitive
		implementation(libs.androidx.composeUiToolingPreview) // Transitive
		implementation(libs.androidx.composeUiUnit) // Transitive
		implementation(libs.androidx.constraintLayoutCompose)
		implementation(libs.kotlinx.coroutinesCore) // Transitive
	}
	
	sourceSets.getByName("androidMain").dependencies {
		implementation(project.dependencies.platform(libs.firebaseBom))
		runtimeOnly(libs.androidx.composeUiTooling)
	}
}
