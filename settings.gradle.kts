@file:Suppress("UnstableApiUsage")

include(":app")
include(":core-detekt")
include(":common")
include(":feature-charges")
include(":feature-companies")
include(":feature-insolvencies")
include(":feature-main")
include(":feature-officers")
include(":feature-persons")
include(":feature-insolvencies")
include(":feature-filings")
include(":shared")

pluginManagement {
	repositories {
		gradlePluginPortal()
		google()
		mavenCentral()
	}
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
	repositories {
		mavenLocal()
		mavenCentral()
		google()
		maven("https://jitpack.io")
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}
