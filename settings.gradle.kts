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
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		exclusiveContent {
			forRepository { google() }
			filter {
				includeGroupAndSubgroups("androidx")
				includeGroupAndSubgroups("com.android")
				includeGroupAndSubgroups("com.google.android")
				includeGroup("com.google.firebase")
				includeGroup("com.google.testing.platform")
			}
		}

		exclusiveContent {
			forRepository { mavenLocal() }
			filter {
				includeGroup("com.babestudios")
			}
		}
		maven("https://jitpack.io")
		maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	}
}
