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

		project.plugins.apply("kotlin-parcelize")

		project.dependencies {
			add("implementation", project.project(":domain"))
			add("implementation", project.project(":common"))

			add("api", Libs.MviKotlin.core)
			add("api", Libs.MviKotlin.coroutines)

			add("implementation", Libs.baBeStudiosBase).apply {
				exclude("androidx.navigation","navigation-fragment-ktx")
				exclude("androidx.navigation","navigation-ui-ktx")
			}
			//LifeCycle, LifecycleOwner, RepeatOnLifecycle
			//add("implementation", Libs.AndroidX.Lifecycle.runtimeKtx)
			add("implementation", Libs.KotlinResult.result)
			add("implementation", Libs.MviKotlin.main)
			add("implementation", Libs.MviKotlin.rx)
			add("implementation", Libs.MviKotlin.logging)

			add("implementation", Libs.AndroidX.Compose.Ui.ui)
			add("implementation", Libs.AndroidX.Compose.Ui.graphics)
			add("implementation", Libs.AndroidX.Compose.Ui.text)
			add("implementation", Libs.AndroidX.Compose.Ui.unit)
			add("implementation", Libs.AndroidX.Compose.Ui.toolingPreview)

			add("testImplementation", Libs.Test.jUnit)
			add("testImplementation", Libs.Test.MockK.core)
			add("testImplementation", Libs.Kotlin.Coroutines.test)
			add("testImplementation", Libs.Test.Kotest.assertionsCore)
			add("testImplementation", Libs.Test.Kotest.assertionsShared)

		}

		val androidExtension = project.extensions.getByName("android")

		if (androidExtension is BaseExtension) {

			androidExtension.apply {
				buildFeatures.compose = true
				buildFeatures.viewBinding = true
				composeOptions.kotlinCompilerExtensionVersion = "1.4.2"
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
