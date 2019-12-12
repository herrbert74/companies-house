package com.babestudios.companyinfouk.plugins.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * This plugin should be used for all android modules.
 * It contains all the default dependencies and settings, that are used in every Android module.
 */
open class BaBeStudiosAndroidPlugin : Plugin<Project> {
	override fun apply(project: Project) {

		if (project.name == "app") {
			project.plugins.apply("com.android.application")
		} else {
			project.plugins.apply("com.android.library")
		}

		project.plugins.apply("kotlin-android")
		project.plugins.apply("kotlin-android-extensions")
		if (project.name != "navigation" && project.name != "common") {
			project.plugins.apply("kotlin-kapt")
		}
		val androidExtension = project.extensions.getByName("android")

		if (androidExtension is BaseExtension) {

			androidExtension.apply {
				compileSdkVersion(29)
				buildToolsVersion = "29.0.2"

				defaultConfig {
					minSdkVersion(21)
					targetSdkVersion(29)
					consumerProguardFiles("consumer-rules.pro")
				}

				buildTypes {
					getByName("release") {
						isMinifyEnabled = false
						proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
					}
				}

				compileOptions {
					sourceCompatibility = JavaVersion.VERSION_1_8
					targetCompatibility = JavaVersion.VERSION_1_8
				}
			}
		}

		project.dependencies {
			add("detekt", "io.gitlab.arturbosch.detekt:detekt-cli:1.2.2")
			add("detektPlugins", project.project(":core-detekt"))
		}
	}
}