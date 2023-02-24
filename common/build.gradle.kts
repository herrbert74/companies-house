import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
}

@Suppress("UnstableApiUsage")
android {
	namespace = "com.babestudios.companyinfouk.common"
	buildFeatures.viewBinding = true
	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.4.2"
}

dependencies {
	implementation(project(":domain"))
	implementation(Libs.baBeStudiosBase)
	implementation(Libs.AndroidX.appcompat)
	implementation(platform(Libs.AndroidX.Compose.bom))
	implementation(Libs.AndroidX.Compose.material3)
	implementation(Libs.AndroidX.Compose.Ui.tooling)
	implementation(Libs.AndroidX.constraintLayoutCompose)
	implementation(Libs.Google.material)
	implementation(Libs.Kotlin.Coroutines.core)
	implementation(Libs.Decompose.core)
	implementation(Libs.Decompose.extensionsJetBrains)
	implementation(Libs.MviKotlin.core)
	implementation(Libs.MviKotlin.rx)
	implementation(Libs.Views.collapsingToolbar)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
}
