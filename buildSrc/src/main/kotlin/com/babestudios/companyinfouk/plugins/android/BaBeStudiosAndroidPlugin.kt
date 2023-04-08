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
				compileSdkVersion(SdkVersions.compileSdkVersion)

				defaultConfig {
					minSdk = SdkVersions.minSdkVersion
					targetSdk = SdkVersions.targetSdkVersion
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
					sourceCompatibility = JavaVersion.VERSION_11
					targetCompatibility = JavaVersion.VERSION_11
				}
			}
		}

		project.dependencies {

			libs.findLibrary("koin.core").ifPresent { add("implementation", it) }
			libs.findLibrary("koin.android").ifPresent { add("implementation", it) }
			if (project.name == "app") {
				libs.findLibrary("inject").ifPresent { add("implementation", it) }
			} else if (project.name != "common") {
				libs.findLibrary("inject").ifPresent { add("api", it) }
			}

			libs.findLibrary("timber").ifPresent { add("implementation", it) }
			libs.findLibrary("detekt.cli").ifPresent { add("detekt", it) }
			libs.findLibrary("google.firebase.bom").ifPresent { add("implementation", platform(it)) }
			libs.findLibrary("google.firebase.analytics").ifPresent { add("implementation", it) }

			add("detektPlugins", project.project(":core-detekt"))

		}

	}
}