@file:Suppress("UnstableApiUsage")

include(":app")
include(":data")
include(":core-detekt")
include(":common")
include(":domain")
include(":feature-charges")
include(":feature-companies")
include(":feature-insolvencies")
include(":feature-officers")
include(":feature-persons")
include(":feature-insolvencies")
include(":feature-filings")

pluginManagement {
	repositories {
		gradlePluginPortal()
		google()
		mavenCentral()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		google()
		maven("https://jitpack.io")
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}
