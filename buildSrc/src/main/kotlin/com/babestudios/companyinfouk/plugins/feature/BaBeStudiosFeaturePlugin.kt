package com.babestudios.companyinfouk.plugins.feature

import com.android.build.gradle.BaseExtension
import com.babestudios.companyinfouk.buildsrc.Libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * This plugin should be used for all features.
 * It contains all the default dependencies, that are used in every feature module.
 */
open class BaBeStudiosFeaturePlugin : Plugin<Project> {
	override fun apply(project: Project) {

		project.plugins.apply("kotlin-kapt")
		project.plugins.apply("dagger.hilt.android.plugin")
		project.plugins.apply("androidx.navigation.safeargs.kotlin")

		project.dependencies {
			add("implementation", project.project(":domain"))
			add("implementation", project.project(":navigation"))
			add("implementation", project.project(":data"))

			add("api", Libs.Google.Dagger.core)
			add("api", Libs.Google.Dagger.Hilt.android)
			add("api", Libs.MviKotlin.core)
			add("api", Libs.MviKotlin.coroutines)

			add("implementation", Libs.AndroidX.constraintLayout)
			add("implementation", Libs.AndroidX.Lifecycle.runtimeKtx)
			add("implementation", Libs.AndroidX.Navigation.uiKtx)
			add("implementation", Libs.AndroidX.Navigation.fragment)
			add("implementation", Libs.KotlinResult.result)
			add("implementation", Libs.RxJava2.rxAndroid)
			add("implementation", Libs.RxJava2.rxJava)
			add("implementation", Libs.Views.multiStateView)
			add("implementation", Libs.MviKotlin.main)
			add("implementation", Libs.MviKotlin.rx)
			add("implementation", Libs.MviKotlin.utilsInternal)
			add("implementation", Libs.MviKotlin.logging)
			add("implementation", Libs.MviKotlin.rxInternal)
			add("implementation", Libs.Views.FlowBinding.android)

			add("testImplementation", Libs.AndroidX.Test.Ext.jUnit)
			add("testImplementation", Libs.Test.MockK.core)
			add("testImplementation", Libs.Kotlin.Coroutines.test)
			add("testImplementation", Libs.Test.Kotest.assertions)

			add("kapt", Libs.Google.Dagger.compiler)
			add("kapt", Libs.Google.Dagger.Hilt.compiler)

			add("kaptTest", Libs.Google.Dagger.compiler)
			add("kaptTest", Libs.Google.Dagger.Hilt.compiler)

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
