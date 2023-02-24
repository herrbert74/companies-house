import com.babestudios.companyinfouk.PluginGradleProperty
import com.babestudios.companyinfouk.script.PropertyResolver
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
	val kotlinVersion = "1.8.10"
	repositories {
		mavenCentral()
		google()
		gradlePluginPortal()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		//classpath("com.android.tools.build:gradle:7.4.1")
		classpath("com.android.tools.build:gradle:7.3.1") //For IJ plugin runIde
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("com.google.gms:google-services:4.3.15")
		classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0")
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
		classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.4")
	}
}

plugins {
	id("io.gitlab.arturbosch.detekt").version("1.19.0")
	id("scabbard.gradle") version "0.5.0"
	/**
	 * Disabled by default
	 * https://blog.dipien.com/improve-your-gradle-build-times-by-only-applying-needed-plugins-5cbe78319e17
	 *
	 * TLDR: Enable them by running the task like this:
	 * ./gradlew dependencyUpdates -PBEN_MANES_VERSIONS_ENABLED=true
	 * ./gradlew buildHealth -PDEPENDENCY_ANALYSIS_ENABLED=true
	 * ./gradlew assembleMirrorDebug -PGRADLE_DOCTOR_ENABLED=true
	 * ./gradlew projectDependencyGraph -PPROJECT_DEPENDENCY_GRAPH_ENABLED=true
	 */
	id("com.github.ben-manes.versions") version "0.44.0" apply false
	id("com.autonomousapps.dependency-analysis") version "1.19.0" apply false
	id("com.osacky.doctor") version "0.8.1" apply false

}

val propertyResolver by extra(PropertyResolver(project))

if (propertyResolver.getBooleanProp(PluginGradleProperty.BEN_MANES_VERSIONS_ENABLED)) {
	apply(plugin = "com.github.ben-manes.versions")
}

if (propertyResolver.getBooleanProp(PluginGradleProperty.DEPENDENCY_ANALYSIS_ENABLED)) {
	apply(plugin = "com.autonomousapps.dependency-analysis")
}

if (propertyResolver.getBooleanProp(PluginGradleProperty.GRADLE_DOCTOR_ENABLED)) {
	apply(plugin = "com.osacky.doctor")
}

if (propertyResolver.getBooleanProp(PluginGradleProperty.PROJECT_DEPENDENCY_GRAPH_ENABLED)) {
	apply("https://raw.githubusercontent.com/JakeWharton/SdkSearch/master/gradle/projectDependencyGraph.gradle")
}

fun teamPropsFile(propsFile: String): File {
	val teamPropsDir = file("team-props")
	return File(teamPropsDir, propsFile)
}

afterEvaluate {
	tasks["detekt"].dependsOn(":core-detekt:assemble")
}

// region Gradle Versions Plugin

fun isNonStable(version: String): Boolean {
	val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
	val regex = "^[0-9,.v-]+(-r)?$".toRegex()
	val isStable = stableKeyword || regex.matches(version)
	return isStable.not()
}

/**
// Exclude updates where candidate version is not stable but current version is stable,
// or candidate is not under our control.
 **/
tasks.withType<DependencyUpdatesTask> {
	resolutionStrategy {
		componentSelection {
			all {
				if ((isNonStable(candidate.version)
						&& !isNonStable(currentVersion))
					|| this.candidate.displayName.contains("desugar")
					|| this.candidate.displayName.contains("aapt2")
					|| this.candidate.displayName.contains("jacoco")) {
					reject("Release candidate")
				}
			}
		}
	}
}

// endregion

scabbard {
	enabled = true
}

//Only works if applied :(
//dependencyAnalysis {
//	issues {
//		all {
//			onAny {
//				exclude(
//					"com.arkivanov.decompose:decompose",
//					"com.arkivanov.decompose:decompose-android-debug",
//					"com.arkivanov.decompose:decompose-android",
//					"com.arkivanov.mvikotlin:mvikotlin",
//					"com.arkivanov.mvikotlin:mvikotlin-android-debug",
//					"com.arkivanov.mvikotlin:mvikotlin-android",
//					"com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines",
//					"com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines-android-debug",
//					"com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines-android",
//					"com.arkivanov.mvikotlin:mvikotlin-logging",
//					"com.arkivanov.mvikotlin:mvikotlin-logging-android-debug",
//					"com.arkivanov.mvikotlin:mvikotlin-logging-android",
//					"com.arkivanov.mvikotlin:mvikotlin-main",
//					"com.arkivanov.mvikotlin:mvikotlin-main-android-debug",
//					"com.arkivanov.mvikotlin:mvikotlin-main-android",
//					"com.arkivanov.mvikotlin:rx",
//					"com.arkivanov.mvikotlin:rx-android-debug",
//					"com.arkivanov.mvikotlin:rx-android",
//					"com.arkivanov.decompose:extensions-compose-jetbrains",
//					"com.arkivanov.decompose:extensions-compose-jetbrains-android-debug",
//					"com.arkivanov.decompose:extensions-compose-jetbrains-android",
//					"com.arkivanov.decompose:extensions-compose-jetpack",
//					"com.arkivanov.decompose:extensions-compose-jetpack-android-debug",
//					"com.arkivanov.decompose:extensions-compose-jetpack-android",
//					"com.arkivanov.essenty:back-handler",
//					"com.arkivanov.essenty:back-handler-android-debug",
//					"com.arkivanov.essenty:back-handler-android",
//					"com.arkivanov.essenty:instance-keeper",
//					"com.arkivanov.essenty:instance-keeper-android-debug",
//					"com.arkivanov.essenty:instance-keeper-android",
//					"com.arkivanov.essenty:lifecycle",
//					"com.arkivanov.essenty:lifecycle-android-debug",
//					"com.arkivanov.essenty:lifecycle-android",
//					"com.arkivanov.essenty:state-keeper",
//					"com.arkivanov.essenty:state-keeper-android-debug",
//					"com.arkivanov.essenty:state-keeper-android"
//				)
//			}
//		}
//	}
//}

apply(teamPropsFile("git-hooks.gradle"))
apply(teamPropsFile("setup-root-tasks.gradle"))
