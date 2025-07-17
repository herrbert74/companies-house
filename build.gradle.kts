//import com.babestudios.companyinfouk.PluginGradleProperty
//import com.babestudios.companyinfouk.script.PropertyResolver

plugins {
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.androidLibrary) apply false
	alias(libs.plugins.kotlin.multiplatform) apply false
	alias(libs.plugins.crashlytics) apply false

	id("detekt-convention")

	/**
	 * Plugins disabled by default and applied on request from CLI
	 * https://blog.dipien.com/improve-your-gradle-build-times-by-only-applying-needed-plugins-5cbe78319e17
	 *
	 * TLDR: Enable them by running the task like this:
	 * ./gradlew dependencyUpdates -PBEN_MANES_VERSIONS_ENABLED=true --no-configuration-cache
	 * ./gradlew buildHealth -PDEPENDENCY_ANALYSIS_ENABLED=true --no-configuration-cache
	 * ./gradlew assembleDebug -PGRADLE_DOCTOR_ENABLED=true
	 * ./gradlew projectDependencyGraph -PPROJECT_DEPENDENCY_GRAPH_ENABLED=true
	 */
//	alias(libs.plugins.versions) apply false
	//Capability conflicts with Skie!!!
	alias(libs.plugins.dependencyAnalysis) apply true
	alias(libs.plugins.gradleDoctor) apply false
	//alias(libs.plugins.kotlin.multiplatform) apply false
//	alias(libs.plugins.android.kotlin.multiplatform.library) apply false
}

//val propertyResolver by extra(PropertyResolver(project))
//
//if (propertyResolver.getBooleanProp(PluginGradleProperty.BEN_MANES_VERSIONS_ENABLED)) {
//	apply(plugin = "com.github.ben-manes.versions")
//}
//
//if (propertyResolver.getBooleanProp(PluginGradleProperty.DEPENDENCY_ANALYSIS_ENABLED)) {
//	apply(plugin = "com.autonomousapps.dependency-analysis")
//}
//
//if (propertyResolver.getBooleanProp(PluginGradleProperty.GRADLE_DOCTOR_ENABLED)) {
//	apply(plugin = "com.osacky.doctor")
//}
//
//if (propertyResolver.getBooleanProp(PluginGradleProperty.PROJECT_DEPENDENCY_GRAPH_ENABLED)) {
//	apply("https://raw.githubusercontent.com/JakeWharton/SdkSearch/master/gradle/projectDependencyGraph.gradle")
//}

fun teamPropsFile(propsFile: String): File {
	val teamPropsDir = file("team-props")
	return File(teamPropsDir, propsFile)
}

//This is needed for custom rules, which is unused now. Detekt is not applied here, so this does not work anymore.
//afterEvaluate {
//	tasks["detekt"].dependsOn(":core-detekt:assemble")
//}

//Only works if applied :(
dependencyAnalysis {
	issues {
		all {
			onAny {
				exclude(
//					"io.mockk:mockk-android", //Wants to remove it
					"com.diamondedge:logging", //Not used everywhere yet, but occasionally needed
				)
			}
		}
	}
}

apply(teamPropsFile("git-hooks.gradle"))
apply(teamPropsFile("setup-root-tasks.gradle"))
