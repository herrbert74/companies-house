plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
}

android.namespace = "com.babestudios.companyinfouk.filings"

dependencies {

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.activity.compose)
	api(libs.androidx.appcompat)
	api(libs.androidx.compose.runtime) //Transitive
	api(libs.kotlinx.coroutines.core) //Transitive
	api(libs.squareUp.okhttp3.okhttp)

	implementation(libs.androidx.activity.activity) //Transitive
	implementation(libs.androidx.constraintLayout.compose)
	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.compose.foundation) //Transitive
	implementation(libs.androidx.compose.foundationLayout)
	implementation(libs.androidx.compose.material.icons.core) //Transitive
	implementation(libs.androidx.compose.runtime.saveable) //Transitive
	implementation(libs.androidx.core) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensionsJetBrains)
	implementation(libs.decompose.extensionsJetpack)
	implementation(libs.uriKmp)
	implementation(libs.ktor.client.core)
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
