import com.babestudios.companyinfouk.androidLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.androidKotlinMultiPlatformLibrary)
	alias(libs.plugins.dependencyAnalysis)
}

extensions.configure<KotlinMultiplatformExtension> {
	androidLibrary {
		compileSdk = libs.versions.compileSdkVersion.get().toInt()
		minSdk = libs.versions.minSdkVersion.get().toInt()
		androidResources.enable = true
	}

	jvmToolchain(libs.versions.jdk.get().toInt())

	sourceSets.getByName("commonMain").dependencies {
		implementation(libs.diamondedge.logging)
	}
}
