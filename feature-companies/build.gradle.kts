import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.babestudios.companyinfouk.plugins.feature")
	id("kotlin-parcelize")
}

dependencies {
	implementation(project(":data"))
	implementation(Libs.Google.PlayServices.maps)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}
