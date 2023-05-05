plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
	kotlin("plugin.serialization").version("1.8.20")
	alias(libs.plugins.ktorfit)
	alias(libs.plugins.ksp)
}

configure<de.jensklingenberg.ktorfit.gradle.KtorfitGradleConfiguration> {
	version = libs.versions.ktorfit.get()
}

val companiesHouseApiKey: String by project

@Suppress("UnstableApiUsage")
android {

	namespace = "com.babestudios.companyinfouk.data"

	buildFeatures.buildConfig = true

	buildTypes {
		all {
			buildConfigField("String", "COMPANIES_HOUSE_API_KEY", companiesHouseApiKey)
		}
	}

}

dependencies {

	api(project(":domain"))

	api(libs.squareUp.okhttp3.okhttp) //Transitive
	api(libs.kotlinx.serialization.core) //Transitive

	implementation(libs.androidx.annotation) //Transitive
	implementation(libs.baBeStudios.base.kotlin)
	implementation(libs.baBeStudios.base.data)
	implementation(libs.google.gson) //Transitive from Base
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinResult.result)
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.ktor.client.okhttp)
	implementation(libs.ktor.client.core)
	implementation(libs.ktor.client.content.negotiation)
	implementation(libs.ktor.client.logging)
	implementation(libs.ktor.http) //Transitive
	implementation(libs.ktor.serialization) //Transitive
	implementation(libs.ktor.utils) //Transitive
	implementation(libs.ktor.serialization.kotlinx.json)
	implementation(libs.ktorfit.lib)
	implementation(libs.kotlinx.serialization.json)

	ksp(libs.ktorfit.ksp)

	debugImplementation(libs.chucker.library)
	releaseImplementation(libs.chucker.noop)

	testImplementation(libs.bundles.mockk.unit)

	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.ktor.client.mock)
	testImplementation(libs.test.jUnit)
	testImplementation(libs.test.kotest.assertions.core)
	testImplementation(libs.test.kotest.assertions.shared)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}
