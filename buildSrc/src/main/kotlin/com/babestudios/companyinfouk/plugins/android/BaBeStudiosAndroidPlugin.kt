package com.babestudios.companyinfouk.plugins.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import com.babestudios.companyinfouk.buildsrc.Libs
import com.babestudios.companyinfouk.buildsrc.Versions

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
				compileSdkVersion(Versions.compileSdkVersion)

				defaultConfig {
					minSdk = Versions.minSdkVersion
					targetSdk = Versions.targetSdkVersion
					consumerProguardFiles("consumer-rules.pro")
				}

				buildTypes {
					getByName("release") {
						isMinifyEnabled = false
						proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
					}
				}
			}
		}

		project.dependencies {
			add("implementation", platform(Libs.Google.Firebase.bom))
			add("implementation", Libs.Google.Firebase.analytics)
			add("detekt", "io.gitlab.arturbosch.detekt:detekt-cli:1.19.0")
			add("detektPlugins", project.project(":core-detekt"))
		}
	}
}