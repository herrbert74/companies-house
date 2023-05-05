package com.babestudios.companyinfouk.plugins.feature

import com.android.build.gradle.BaseExtension
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.artifacts.ModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.accessors.runtime.addConfiguredDependencyTo
import org.gradle.kotlin.dsl.dependencies

//We need to copy these here to correctly retrieve dependencies for exclude
fun DependencyHandler.implementation(
	dependencyNotation: Provider<*>,
	dependencyConfiguration: Action<ExternalModuleDependency>,
): Unit = addConfiguredDependencyTo(
	this, "implementation", dependencyNotation, dependencyConfiguration
)

fun DependencyHandler.api(
	dependencyNotation: Provider<*>,
	dependencyConfiguration: Action<ExternalModuleDependency>,
): Unit = addConfiguredDependencyTo(
	this, "api", dependencyNotation, dependencyConfiguration
)

/**
 * This plugin should be used for all features.
 * It contains all the default dependencies, that are used in every feature module.
 */
open class BaBeStudiosFeaturePlugin : Plugin<Project> {

	@Suppress("UnstableApiUsage")
	override fun apply(project: Project) {

		val catalogs = project.extensions.getByType(VersionCatalogsExtension::class.java)
		val libs = catalogs.named("libs")

		project.dependencies {
			add("api", project.project(":domain"))
			add("implementation", project.project(":common"))

			libs.findLibrary("mvikotlin.core").ifPresent { add("api", it) }
			libs.findLibrary("mvikotlin.coroutines").ifPresent { add("api", it) }

			libs.findLibrary("baBeStudios.base.compose").ifPresent { add("implementation", it) }
			libs.findLibrary("baBeStudios.base.kotlin").ifPresent { add("testImplementation", it) }

			//LifeCycle, LifecycleOwner, RepeatOnLifecycle; NOT USED for now
			//libs.findLibrary("androidx.lifecycle.runtimeKtx").ifPresent { add("implementation", it) }

			libs.findLibrary("kotlinResult.result").ifPresent { add("implementation", it) }

			libs.findLibrary("mvikotlin.main").ifPresent { add("implementation", it) }
			libs.findLibrary("mvikotlin.rx").ifPresent { add("implementation", it) }
			libs.findLibrary("mvikotlin.logging").ifPresent { add("implementation", it) }

			libs.findLibrary("androidx.compose.ui").ifPresent { add("implementation", it) }
			libs.findLibrary("androidx.compose.ui.graphics").ifPresent { add("implementation", it) }
			libs.findLibrary("androidx.compose.ui.text").ifPresent { add("implementation", it) }
			libs.findLibrary("androidx.compose.ui.unit").ifPresent { add("implementation", it) }
			libs.findLibrary("androidx.compose.ui.tooling").ifPresent { add("runtimeOnly", it) }
			libs.findLibrary("androidx.compose.ui.toolingPreview").ifPresent { add("implementation", it) }
			libs.findLibrary("androidx.compose.material3").ifPresent { add("implementation", it) }

//	An exclude example
//			libs.findLibrary("androidx.compose.material3").ifPresent {
//				implementation(it){
//					exclude("androidx.compose.material3", "material3-android")
//				}
//			}

			libs.findLibrary("test.jUnit").ifPresent { add("testImplementation", it) }
			libs.findBundle("mockk.unit").ifPresent { add("testImplementation", it) }
			libs.findLibrary("kotlinx.coroutines.test").ifPresent { add("testImplementation", it) }
			libs.findLibrary("test.kotest.assertions.shared").ifPresent { add("testImplementation", it) }

		}

		val androidExtension = project.extensions.getByName("android")

		if (androidExtension is BaseExtension) {

			androidExtension.apply {
				buildFeatures.compose = true
				composeOptions.kotlinCompilerExtensionVersion =
					libs.findVersion("androidx.compose.compiler").get().toString()
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
