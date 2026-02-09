import com.babestudios.companyinfouk.androidLibrary
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.ktorfit)
	alias(libs.plugins.mokkery)
	// alias(libs.plugins.touchlab.skie)
	alias(libs.plugins.buildKonfig)
}

val companiesHouseApiKey: String by project

buildkonfig {
	packageName = "com.babestudios.companyinfouk.shared"

	defaultConfigs {
		buildConfigField(STRING, "COMPANIES_HOUSE_API_KEY", companiesHouseApiKey)
	}
}

kotlin {
	androidLibrary {
		namespace = "com.babestudios.companyinfouk.shared"
	}

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64()
	).forEach {
		it.binaries.framework {
			baseName = "shared"
			export(libs.decompose.core)
			export(libs.mvikotlin.core)
			export(libs.essenty.lifecycle)
			export(libs.baBeStudios.baseKotlin)
			export(libs.baBeStudios.baseData)
		}
	}

	compilerOptions {

		// https://youtrack.jetbrains.com/issue/KT-61573
		freeCompilerArgs.add("-Xexpect-actual-classes")

		freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
	}

	sourceSets {

		commonMain.dependencies {

			api(libs.baBeStudios.baseData)
			api(libs.baBeStudios.baseKotlin)
			api(libs.essenty.lifecycle)

			implementation(libs.decompose.core)
			implementation(libs.essenty.backHandler)
			implementation(libs.essenty.instanceKeeper)
			implementation(libs.essenty.stateKeeper)
			implementation(libs.koin.core)
			implementation(libs.kotlinx.coroutinesCore)
			implementation(libs.kotlinx.datetime)
			implementation(libs.kotlinx.serializationCore) // Transitive
			implementation(libs.kotlinResult.result)
			implementation(libs.ktor.clientCore)
			implementation(libs.ktor.clientContentNegotiation)
			implementation(libs.ktor.clientLogging)
			implementation(libs.ktor.io) // Transitive
			implementation(libs.ktor.http) // Transitive
			implementation(libs.ktor.serialization) // Transitive
			implementation(libs.ktor.utils) // Transitive
			implementation(libs.ktor.serializationKotlinxJson)
			implementation(libs.ktorfit.annotations)
			implementation(libs.ktorfit.libLight)
			implementation(libs.kotlinx.serializationJson)
			implementation(libs.mvikotlin.core)
			implementation(libs.mvikotlin.coroutines)
			implementation(libs.mvikotlin.main)
			implementation(libs.mvikotlin.logging)
			implementation(libs.diamondedge.logging)
			implementation(libs.uriKmp)
			implementation(libs.multiplatformSettings.core)
			implementation(libs.multiplatformSettings.noargs)
		}

		commonTest.dependencies {
			implementation(libs.kotlin.test)
			implementation(libs.kotlinx.coroutinesTest)
			implementation(libs.ktor.clientMock)
			implementation(libs.jUnit)
			implementation(libs.kotest.assertionsCore)
			implementation(libs.kotest.assertionsShared)
		}

		androidMain.dependencies {
			api(libs.okhttp3)

			implementation(libs.koin.core)
			implementation(libs.koin.android)
			implementation(project.dependencies.platform(libs.firebaseBom))
			implementation(libs.firebaseAnalytics)
			implementation(libs.ktor.clientOkhttp)
			implementation(libs.jUnit)
			implementation(libs.chucker.noop)
		}

		androidUnitTest.dependencies {
			implementation(libs.jUnit)
		}
		iosMain.dependencies {
			api(libs.decompose.core)
			api(libs.mvikotlin.core)
			api(libs.essenty.lifecycle)
		}

		all {
			languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
		}

	}
}

/**
 * Temporary fix for Ktorfit KSP issue, which is still not resolved in Ktorfit 2.7.2
 * https://github.com/Foso/Ktorfit/issues/1010
 */
ktorfit {
	compilerPluginVersion.set("2.3.3")
}

// region KSP commonMain configuration
// https://github.com/google/ksp/issues/567
// Workaround from here:
// https://github.com/google/ksp/issues/567#issuecomment-1510477456

kotlin.sourceSets.commonMain {
	kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}

tasks.withType<KotlinJvmCompile> {
	if (name.startsWith("compileTestKotlin")) {
		dependsOn("kspTestKotlinJvm")
	}
	if (name != "kspCommonMainKotlinMetadata") {
		dependsOn("kspCommonMainKotlinMetadata")
	}
}

tasks {
	configureEach {
		if (this.name.contains("kspDebugKotlinAndroid")) {
			this.dependsOn("kspCommonMainKotlinMetadata")
		}
	}
}

// endregion
