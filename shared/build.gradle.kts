plugins {
	kotlin("multiplatform")
	id("com.android.library")
	id("kotlin-parcelize")
	kotlin("plugin.serialization").version("1.8.20")
	alias(libs.plugins.parcelize.darwin)
	alias(libs.plugins.ksp)
	alias(libs.plugins.ktorfit)
}

kotlin {
	android {
		compilations.all {
			kotlinOptions {
				jvmTarget = "17"
			}
		}
	}

	listOf(
		iosX64(),
		iosArm64(),
		iosSimulatorArm64()
	).forEach {
		it.binaries.framework {
			baseName = "shared"
		}
	}

	sourceSets {

		val commonMain by getting {
			dependencies {
				//Domain

				/**
				 * TODO Use base-kotlin
				 * https://github.com/jitpack/jitpack.io/issues/3853
				 */
				//api(libs.baBeStudios.base.kotlin)

				implementation(libs.koin.core)
				implementation(libs.kotlin.parcelize.runtime) //Transitive
				implementation(libs.kotlinx.coroutines.core)
				implementation(libs.kotlinx.serialization.core) //Transitive
				implementation(libs.kotlinResult.result)

				//Data
				//implementation(libs.androidx.annotation) //Transitive
				//implementation(libs.baBeStudios.base.kotlin)
				//implementation(libs.baBeStudios.base.data)
				//implementation(libs.google.gson) //Transitive from Base
				implementation(libs.kotlinResult.result)
				implementation(libs.koin.core)
				implementation(libs.ktor.client.core)
				implementation(libs.ktor.client.content.negotiation)
				implementation(libs.ktor.client.logging)
				implementation(libs.ktor.http) //Transitive
				implementation(libs.ktor.serialization) //Transitive
				implementation(libs.ktor.utils) //Transitive
				implementation(libs.ktor.serialization.kotlinx.json)
				implementation(libs.ktorfit.lib)
				implementation(libs.kotlinx.serialization.json)
				implementation(libs.logging)
				implementation(libs.uriKmp)

				//ksp(libs.ktorfit.ksp)

//
//				testImplementation(libs.bundles.mockk.unit)
//
//				testImplementation(libs.kotlinx.coroutines.test)
//				testImplementation(libs.ktor.client.mock)
//				testImplementation(libs.test.jUnit)
//				testImplementation(libs.test.kotest.assertions.core)
//				testImplementation(libs.test.kotest.assertions.shared)

			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))
			}
		}
		val androidMain by getting {
			dependencies {

				//data
				implementation(libs.koin.core)
				implementation(libs.koin.android)

				api(libs.squareUp.okhttp3.okhttp)
				//implementation(libs.kotlinx.coroutines.core)
				implementation(libs.ktor.client.okhttp)

			}
		}
		val androidUnitTest by getting
		val iosX64Main by getting
		val iosArm64Main by getting
		val iosSimulatorArm64Main by getting
		val iosMain by creating {
			dependsOn(commonMain)
			iosX64Main.dependsOn(this)
			iosArm64Main.dependsOn(this)
			iosSimulatorArm64Main.dependsOn(this)

			dependencies {
				implementation(libs.parcelize.darwin.runtime)
			}
		}
		val iosX64Test by getting
		val iosArm64Test by getting
		val iosSimulatorArm64Test by getting
		val iosTest by creating {
			dependsOn(commonTest)
			iosX64Test.dependsOn(this)
			iosArm64Test.dependsOn(this)
			iosSimulatorArm64Test.dependsOn(this)
		}
	}
}

android {
	namespace = "com.babestudios.companyinfouk.shared"
	compileSdk = 33
	defaultConfig {
		minSdk = 21
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	dependencies {
		debugImplementation(libs.chucker.library)
		releaseImplementation(libs.chucker.noop)
	}
}

dependencies {
	add("kspCommonMainMetadata", libs.ktorfit.ksp)
	add("kspAndroid", libs.ktorfit.ksp)
}
