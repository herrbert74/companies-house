package com.babestudios.companyinfouk.plugins.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.apply

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

		project.apply(from = project.rootProject.file("team-props/detekt/detekt.gradle"))

		val androidExtension = project.extensions.getByName("android")

		val catalogs = project.extensions.getByType(VersionCatalogsExtension::class.java)
		val libs = catalogs.named("libs")

		if (androidExtension is BaseExtension) {

			androidExtension.apply {
				libs.findVersion("compileSdkVersion").ifPresent { compileSdkVersion(it.toString().toInt()) }

				defaultConfig {
					libs.findVersion("minSdkVersion").ifPresent { minSdk = it.toString().toInt() }
					libs.findVersion("targetSdkVersion").ifPresent { targetSdk = it.toString().toInt() }
					consumerProguardFiles("consumer-rules.pro")
				}

				buildTypes {
					getByName("release") {
						isMinifyEnabled = false
						proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
					}
				}

				//We should use toolChain, but has no effect yet: https://issuetracker.google.com/issues/260059413
				compileOptions {
					sourceCompatibility = JavaVersion.VERSION_21
					targetCompatibility = JavaVersion.VERSION_21
				}
			}
		}

		project.dependencies {

			libs.findLibrary("logging").ifPresent { add("implementation", it) }
			libs.findLibrary("detekt.cli").ifPresent { add("detekt", it) }
			libs.findLibrary("google.firebase.bom").ifPresent { add("implementation", platform(it)) }
			libs.findLibrary("google.firebase.analytics").ifPresent { add("implementation", it) }

			add("detektPlugins", project.project(":core-detekt"))

		}

	}

}
