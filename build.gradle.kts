import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
	extra["kotlin_version"] = "1.8.0"
	val kotlinVersion = "1.8.0"
	repositories {
		mavenCentral()
		google()
		gradlePluginPortal()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("com.android.tools.build:gradle:7.4.1")
		//classpath("com.android.tools.build:gradle:7.3.1") //For IJ plugin runIde
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("com.google.gms:google-services:4.3.15")
		classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0")
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
		classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
	}
}

plugins {
	id("io.gitlab.arturbosch.detekt").version("1.19.0")
	id("com.github.ben-manes.versions").version("0.42.0")
	id("scabbard.gradle") version "0.5.0"
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

apply(teamPropsFile("git-hooks.gradle"))
apply(teamPropsFile("setup-root-tasks.gradle"))
