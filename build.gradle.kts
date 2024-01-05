import com.babestudios.companyinfouk.PluginGradleProperty
import com.babestudios.companyinfouk.script.PropertyResolver
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
	dependencies {
		classpath(libs.androidGradlePlugin)
		classpath(libs.kotlinPlugin)
		classpath(libs.kotlinAllOpenPlugin)
		classpath(libs.googleServicesPlugin)
	}
}

@Suppress("DSL_SCOPE_VIOLATION") //https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
	alias(libs.plugins.crashlytics) apply false
	id("scabbard.gradle") version "0.5.0"

	//We apply it in BaBeStudiosAndroidPlugin -> detekt.gradle
	alias(libs.plugins.detekt) apply false

	/**
	 * Plugins disabled by default and applied on request from CLI
	 * https://blog.dipien.com/improve-your-gradle-build-times-by-only-applying-needed-plugins-5cbe78319e17
	 *
	 * TLDR: Enable them by running the task like this:
	 * ./gradlew dependencyUpdates -PBEN_MANES_VERSIONS_ENABLED=true --no-configuration-cache
	 * ./gradlew buildHealth -PDEPENDENCY_ANALYSIS_ENABLED=true --no-configuration-cache
	 * ./gradlew assembleMirrorDebug -PGRADLE_DOCTOR_ENABLED=true
	 * ./gradlew projectDependencyGraph -PPROJECT_DEPENDENCY_GRAPH_ENABLED=true
	 */
	alias(libs.plugins.versions) apply false
	//Capability conflicts with Skie!!!
	alias(libs.plugins.dependencyAnalyis) apply false
	alias(libs.plugins.gradleDoctor) apply false
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

//This is needed for custom rules, which is unused now. Detekt is not applied here, so this does not work anymore.
//afterEvaluate {
//	tasks["detekt"].dependsOn(":core-detekt:assemble")
//}

// region Gradle Versions Plugin

fun isNonStable(version: String): Boolean {
	val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
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
//					"io.mockk:mockk-android", //Wants to remove it
//					"org.lighthousegames:logging", //Not used everywhere yet, but occasionally needed
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
