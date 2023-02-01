import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
	id("kotlin-parcelize")
}

dependencies {
	implementation(project(":data"))
	implementation(Libs.Decompose.core)
	implementation(Libs.Decompose.extensionsJetpack)
	implementation(Libs.Decompose.extensionsJetBrains)
	implementation(Libs.AndroidX.Activity.compose)
	implementation(Libs.AndroidX.constraintLayoutCompose)
	implementation(platform(Libs.AndroidX.Compose.bom))
	implementation(Libs.AndroidX.Compose.Ui.tooling)
	implementation(Libs.AndroidX.Compose.material3)
	implementation(Libs.AndroidX.Compose.foundationLayout)
	implementation(Libs.Google.PlayServices.maps)
	implementation(Libs.Google.PlayServices.mapsCompose)
	implementation(Libs.Views.collapsingToolbar)
}

android {

	buildFeatures {
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.0"
	}

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=com.arkivanov.decompose.ExperimentalDecomposeApi"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
}
