import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
	val kotlinVersion = "1.3.61"
	repositories {
		google()
		maven { url = uri("https://maven.fabric.io/public") }
		jcenter()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
		classpath("com.android.tools.build:gradle:4.0.0-alpha05")
		classpath("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
		classpath("com.google.gms:google-services:4.3.3")
		classpath("io.fabric.tools:gradle:1.31.2")
		classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.2.2")
	}
}

plugins {
	id("io.gitlab.arturbosch.detekt").version("1.2.2")
	id("com.github.ben-manes.versions").version("0.27.0")
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

apply(teamPropsFile ("git-hooks.gradle"))
apply(teamPropsFile ("setup-root-tasks.gradle"))
