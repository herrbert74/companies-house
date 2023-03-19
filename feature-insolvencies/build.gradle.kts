plugins{
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
}

android.namespace = "com.babestudios.companyinfouk.insolvencies"

dependencies {
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensionsJetpack)
	implementation(libs.decompose.extensionsJetBrains)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.foundationLayout)
	implementation(libs.view.collapsingToolbar)
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
