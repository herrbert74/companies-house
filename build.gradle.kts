import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
	extra["kotlin_version"] = "1.4.0"
	val kotlinVersion = "1.4.0"
	repositories {
		google()
		jcenter()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("com.android.tools.build:gradle:4.2.0-alpha07")
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("com.google.gms:google-services:4.3.3")
		classpath("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
		classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.6.0")
	}
}

plugins {
	id("io.gitlab.arturbosch.detekt").version("1.6.0")
	id("com.github.ben-manes.versions").version("0.29.0")
}

allprojects {
	repositories {
		google()
		maven { url = uri("https://jitpack.io") }
		jcenter()
	}
	apply("$rootDir/team-props/detekt/detekt.gradle")

	tasks.withType<KotlinCompile> {
		kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
	}
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

apply(teamPropsFile("git-hooks.gradle"))
apply(teamPropsFile("setup-root-tasks.gradle"))
