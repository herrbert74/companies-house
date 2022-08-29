import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
	id("kotlin-parcelize")
}

dependencies {
	implementation(Libs.Decompose.core)
	implementation(Libs.Decompose.extensionsCompose)
	implementation(Libs.AndroidX.Activity.compose)
	implementation(Libs.AndroidX.Compose.Ui.tooling)
	implementation(Libs.AndroidX.Compose.material)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}
