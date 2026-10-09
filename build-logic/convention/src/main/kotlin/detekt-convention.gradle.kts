import dev.detekt.gradle.Detekt

plugins {
	alias(libs.plugins.detekt)
}

/**
 * Detekt configuration for the project.
 * It's enough to apply this script in the root project
 **/
tasks.register<Detekt>("detektAll") {
	description = ""
	parallel = true
	setSource(files(rootDir))
	pluginClasspath.from(configurations.detektPlugins)
	reports {
		checkstyle {
			required = false
		}
		sarif {
			required = false // true by default, despite what docs say
		}
	}
	config = files(
		"$rootDir/team-props/detekt/default-detekt-config.yml",
		"$rootDir/team-props/detekt/compose-detekt-config.yml",
		"$rootDir/team-props/detekt/formatting-detekt-config.yml"
	)
	exclude("**/resources/**")
	exclude("**/build/**")
	exclude("**/bin/**")
}

dependencies {
	detektPlugins(libs.detekt.compose)
	detektPlugins(libs.detekt.formatting)
}
