plugins {
	alias(libs.plugins.androidLibrary)
	id("io.gitlab.arturbosch.detekt")
	id("com.autonomousapps.dependency-analysis")
	kotlin("android")
}

apply(from = project.rootProject.file("team-props/detekt/detekt.gradle"))

android {
	compileSdk = libs.versions.compileSdkVersion.get().toInt()
	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
		consumerProguardFiles("consumer-rules.pro")
	}
}

kotlin {
	jvmToolchain(libs.versions.jdk.get().toInt())
}

dependencies {
	implementation(libs.diamondedge.logging)
	"detekt"(libs.detekt.cli)
	"detektPlugins"(libs.detekt.compose)
}
