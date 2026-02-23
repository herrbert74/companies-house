import com.babestudios.companyinfouk.androidLibrary

plugins {
    id("android-library-convention")
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.composeCompiler) // TODO Move to Feature plugin
    alias(libs.plugins.mokkery)
}

androidLibrary {
	namespace = "com.babestudios.companyinfouk.main"

	withDeviceTest {
		instrumentationRunner = "com.babestudios.companyinfouk.CompaniesHouseAndroidJUnitRunner"
		targetSdk { libs.versions.targetSdkVersion }
	}

	packaging {
		resources.excludes.add("META-INF/LICENSE.md")
		resources.excludes.add("META-INF/LICENSE-notice.md")
	}
}

kotlin {
	sourceSets.getByName("commonMain").dependencies {
		api(project(":shared"))

		implementation(project(":feature-charges"))
		implementation(project(":feature-companies"))
		implementation(project(":feature-filings"))
		implementation(project(":feature-insolvencies"))
		implementation(project(":feature-officers"))
		implementation(project(":feature-persons"))

		implementation(project(":common"))

		api(libs.androidx.activity) // Transitive
		api(libs.kotlinx.coroutinesCore) // Transitive

		implementation(libs.androidx.composeRuntime) // Transitive

		implementation(libs.androidx.activityCompose) // Transitive
		implementation(libs.androidx.composeAnimationCore) // Transitive
		implementation(libs.androidx.composeUi)
		implementation(libs.androidx.lifecycleCommon) // Transitive
		implementation(libs.androidx.lifecycleViewmodel) // Transitive
		implementation(libs.androidx.savedState) // Transitive
		implementation(libs.decompose.core)
		implementation(libs.decompose.extensions)
		implementation(libs.koin.core)
		implementation(libs.koin.android)
		implementation(libs.kotlinx.datetime)
	}

	sourceSets.getByName("androidMain").dependencies {
		runtimeOnly(libs.androidx.composeUiTooling)
		// debugRuntimeOnly(libs.androidx.composeUiTestManifest)
	}
}
