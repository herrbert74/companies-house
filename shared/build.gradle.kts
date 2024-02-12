import com.babestudios.companyinfouk.plugins.android.SdkVersions
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

buildscript {
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath(libs.buildKonfig)
	}
}

plugins {
	kotlin("multiplatform")
	id("com.android.library")
	id("kotlin-parcelize")
	kotlin("plugin.serialization") version libs.versions.kotlin
	alias(libs.plugins.parcelize.darwin)
	alias(libs.plugins.ksp)
	alias(libs.plugins.ktorfit)
	alias(libs.plugins.touchlab.skie)
	id("org.kodein.mock.mockmp") version libs.versions.mockmp
	id("com.codingfeline.buildkonfig") version "0.13.3"
}

val companiesHouseApiKey: String by project

buildkonfig {
	packageName = "com.babestudios.companyinfouk.shared"

	defaultConfigs {
		buildConfigField(STRING, "COMPANIES_HOUSE_API_KEY", companiesHouseApiKey)
	}
}

mockmp {
	usesHelper = true
}

//TODO https://touchlab.co/kotlin-1-9-20-source-set-enhancements
kotlin {
	androidTarget {
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
			export(libs.decompose.core)
			export(libs.mvikotlin.core)
			export(libs.essenty.lifecycle)
			export(libs.baBeStudios.base.kotlin)
			export(libs.baBeStudios.base.data)
		}
	}

	sourceSets {

		val commonMain by getting {
			dependencies {

				api(libs.baBeStudios.base.data)
				api(libs.baBeStudios.base.kotlin)

				implementation(libs.decompose.core)
				implementation(libs.koin.core)
				implementation(libs.kotlin.parcelize.runtime) //Transitive
				implementation(libs.kotlinx.coroutines.core)
				implementation(libs.kotlinx.serialization.core) //Transitive
				implementation(libs.kotlinResult.result)

				//Data
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
				implementation(libs.mvikotlin.core)
				implementation(libs.mvikotlin.coroutines)
				implementation(libs.mvikotlin.main)
				implementation(libs.mvikotlin.rx)
				implementation(libs.mvikotlin.logging)
				implementation(libs.logging)
				implementation(libs.uriKmp)
				implementation(libs.multiplatformSettings.core)
				implementation(libs.multiplatformSettings.noargs)

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

				implementation(libs.test.kotest.assertions.core)
				implementation(libs.test.kotest.assertions.shared)
				implementation(libs.kotlinx.coroutines.test)
				implementation(libs.test.mockmp.runtime)
				implementation(libs.test.mockmp.testHelper)
				implementation(libs.ktor.client.mock)

			}
			kotlin.srcDir("build/generated/ksp/jvm/jvmTest/kotlin") //for mockmp
		}
		val androidMain by getting {

			dependencies {

				//data
				implementation(libs.koin.core)
				implementation(libs.koin.android)
				implementation(project.dependencies.platform(libs.google.firebase.bom))
				implementation(libs.google.firebase.analytics)

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
				api(libs.decompose.core)
				api(libs.mvikotlin.core)
				api(libs.essenty.lifecycle)
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
	compileSdk = SdkVersions.compileSdkVersion

	defaultConfig {
		minSdk = SdkVersions.minSdkVersion
	}

	buildFeatures.buildConfig = true

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
//	add("kspAndroid", libs.ktorfit.ksp)
//	add("kspIosArm64", libs.ktorfit.ksp)
//	add("kspIosSimulatorArm64", libs.ktorfit.ksp)
//	add("kspIosX64", libs.ktorfit.ksp)
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>() {
	if (name.startsWith("compileTestKotlin")) {
		dependsOn("kspTestKotlinJvm")
	}
	//Work around for Cannot change attributes of dependency configuration ... after it has been resolved
	//https://stackoverflow.com/questions/72471375/cannot-change-attributes-of-dependency-configuration-appreleaseunittestcompil
	if(name != "kspCommonMainKotlinMetadata") {
		dependsOn("kspCommonMainKotlinMetadata")
	}
}

//Work around for 'Consumable configurations must have unique attributes'
//https://youtrack.jetbrains.com/issue/KT-55751/MPP-Gradle-Consumable-configurations-must-have-unique-attributes
//val myAttribute: Attribute<String> = Attribute.of("myOwnAttribute", String::class.java)
//configurations.named("releaseFrameworkIosFat").configure {
//	attributes {
//		// put a unique attribute
//		attribute(myAttribute, "release-all")
//	}
//}

//Partial solution for generating common code
//https://github.com/google/ksp/issues/567
kotlin.sourceSets.commonMain {
	kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}
