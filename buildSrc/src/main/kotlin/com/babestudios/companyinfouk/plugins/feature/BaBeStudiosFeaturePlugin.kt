package com.babestudios.companyinfouk.plugins.feature

import com.android.build.gradle.BaseExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project

import com.babestudios.companyinfouk.buildsrc.Libs

/**
 * This plugin should be used for all features.
 * It contains all the default dependencies, that are used in every feature module.
 */
open class BaBeStudiosFeaturePlugin : Plugin<Project> {
	override fun apply(project: Project) {

		project.dependencies {
			add("api", project.project(":core-injection"))

			add("implementation", Libs.MvRx.mvrx)
			add("implementation", Libs.AndroidX.constraintLayout)
			add("implementation", Libs.AndroidX.Navigation.ktx)
			add("implementation", Libs.AndroidX.Navigation.fragment)
			add("implementation", Libs.Google.Dagger.dagger)
			add("implementation", Libs.RxJava2.rxAndroid)
			add("implementation", Libs.RxJava2.rxJava)
			add("implementation", Libs.JakeWharton.RxBinding.core)
			add("implementation", Libs.MvRx.testing)

			add("kapt", Libs.Google.Dagger.compiler)

			add("testImplementation", Libs.AndroidX.Test.Ext.jUnit)
			add("testImplementation", Libs.Test.mockK)
		}

		val androidExtension = project.extensions.getByName("android")

		if (androidExtension is BaseExtension) {

			androidExtension.apply {
				@Suppress("UnstableApiUsage")
				buildFeatures.viewBinding = true
			}
		}
	}
}
