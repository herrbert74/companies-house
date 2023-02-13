package com.babestudios.companyinfouk.plugins.feature

import com.android.build.gradle.BaseExtension
import com.babestudios.companyinfouk.buildsrc.Libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.kotlin.dsl.dependencies

/**
 * This plugin should be used for all features.
 * It contains all the default dependencies, that are used in every feature module.
 */
open class BaBeStudiosFeaturePlugin : Plugin<Project> {

	@Suppress("UnstableApiUsage")
	override fun apply(project: Project) {

		project.plugins.apply("kotlin-kapt")
		project.plugins.apply("kotlin-parcelize")
		project.plugins.apply("dagger.hilt.android.plugin")

		project.dependencies {
			add("implementation", project.project(":domain"))
			add("implementation", project.project(":common"))

			add("api", Libs.Google.Dagger.core)
			add("api", Libs.Google.Dagger.Hilt.android)
			add("api", Libs.MviKotlin.core)
			add("api", Libs.MviKotlin.coroutines)

			add("implementation", Libs.baBeStudiosBase).apply {
				exclude("androidx.navigation","navigation-fragment-ktx")
				exclude("androidx.navigation","navigation-ui-ktx")
			}
			add("implementation", Libs.AndroidX.constraintLayout)
			add("implementation", Libs.AndroidX.Lifecycle.runtimeKtx)
			add("implementation", Libs.KotlinResult.result)
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
				buildFeatures.compose = true
				buildFeatures.viewBinding = true
				composeOptions.kotlinCompilerExtensionVersion = "1.4.0"
			}
		}
	}

}

fun Dependency?.exclude(group: String, module: String) {
	val exclude: MutableMap<String, String> = HashMap()
	exclude["group"] = group
	exclude["module"] = module
	(this as ModuleDependency).exclude(exclude)
}
