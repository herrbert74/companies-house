import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.ksp)
	alias(libs.plugins.ktorfit)
	alias(libs.plugins.mokkery)
	//alias(libs.plugins.touchlab.skie)
	alias(libs.plugins.buildKonfig)
}

val companiesHouseApiKey: String by project

buildkonfig {
	packageName = "com.babestudios.companyinfouk.shared"

	defaultConfigs {
		buildConfigField(STRING, "COMPANIES_HOUSE_API_KEY", companiesHouseApiKey)
	}
}

//TODO https://touchlab.co/kotlin-1-9-20-source-set-enhancements
kotlin {
	jvmToolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
	androidTarget {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_21)
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
				api(libs.essenty.lifecycle)

				implementation(libs.decompose.core)
				implementation(libs.essenty.backHandler)
				implementation(libs.essenty.instanceKeeper)
				implementation(libs.essenty.stateKeeper)
				implementation(libs.klock)
				//implementation(libs.koin.android)
				implementation(libs.koin.core)
				implementation(libs.kotlinx.coroutines.core)
				implementation(libs.kotlinx.serialization.core) //Transitive
				implementation(libs.kotlinResult.result)
				implementation(libs.ktor.client.core)
				implementation(libs.ktor.client.content.negotiation)
				implementation(libs.ktor.client.logging)
				implementation(libs.ktor.io) //Transitive
				implementation(libs.ktor.http) //Transitive
				implementation(libs.ktor.serialization) //Transitive
				implementation(libs.ktor.utils) //Transitive
				implementation(libs.ktor.serialization.kotlinx.json)
				implementation(libs.ktorfit.annotations)
				implementation(libs.ktorfit.lib)
				implementation(libs.ktorfit.lib.light)
				implementation(libs.kotlinx.serialization.json)
				implementation(libs.mvikotlin.core)
				implementation(libs.mvikotlin.coroutines)
				implementation(libs.mvikotlin.main)
				implementation(libs.mvikotlin.logging)
				implementation(libs.logging)
				implementation(libs.uriKmp)
				implementation(libs.multiplatformSettings.core)
				implementation(libs.multiplatformSettings.noargs)
			}
		}
		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))

				implementation(libs.test.jUnit)
				implementation(libs.test.kotest.assertions.core)
				implementation(libs.test.kotest.assertions.shared)
				implementation(libs.kotlinx.coroutines.test)
				implementation(libs.ktor.client.mock)

			}
		}
		val androidMain by getting {

			dependencies {

				api(libs.squareUp.okhttp3.okhttp)

				implementation(libs.koin.core)
				implementation(libs.koin.android)
				implementation(project.dependencies.platform(libs.google.firebase.bom))
				implementation(libs.google.firebase.analytics)
				implementation(libs.ktor.client.okhttp)
				implementation(libs.test.jUnit)

			}
		}
		val androidUnitTest by getting {
			dependencies {
				implementation(libs.test.jUnit)
			}
		}
		val iosX64Main by getting
		val iosArm64Main by getting
		val iosSimulatorArm64Main by getting
		val iosMain by creating {
//			dependsOn(commonMain)
//			iosX64Main.dependsOn(this)
//			iosArm64Main.dependsOn(this)
//			iosSimulatorArm64Main.dependsOn(this)

			dependencies {
				api(libs.decompose.core)
				api(libs.mvikotlin.core)
				api(libs.essenty.lifecycle)
			}
		}
		val iosX64Test by getting
		val iosArm64Test by getting
		val iosSimulatorArm64Test by getting
		val iosTest by creating {
//			dependsOn(commonTest)
//			iosX64Test.dependsOn(this)
//			iosArm64Test.dependsOn(this)
//			iosSimulatorArm64Test.dependsOn(this)
		}

		all {
			languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
		}

	}
}

android {

	namespace = "com.babestudios.companyinfouk.shared"
	compileSdk = libs.versions.compileSdkVersion.get().toInt()

	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
	}

	buildFeatures.buildConfig = true

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
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

//https://youtrack.jetbrains.com/issue/KT-61573
tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).configureEach {
	compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
