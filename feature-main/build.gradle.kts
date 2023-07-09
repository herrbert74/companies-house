plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
}

@Suppress("UnstableApiUsage")
android {

	namespace = "com.babestudios.companyinfouk.main"

	buildFeatures.compose = true

	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
	}

	defaultConfig {
		testInstrumentationRunner = "com.babestudios.companyinfouk.CompaniesHouseAndroidJUnitRunner"
	}

	packaging {
		resources.excludes.add("META-INF/LICENSE.md")
		resources.excludes.add("META-INF/LICENSE-notice.md")
	}

}

dependencies {

	api(project(":shared"))

	implementation(project(":feature-charges"))
	implementation(project(":feature-companies"))
	implementation(project(":feature-filings"))
	implementation(project(":feature-insolvencies"))
	implementation(project(":feature-officers"))
	implementation(project(":feature-persons"))

	implementation(project(":common"))

	api(libs.androidx.activity.activity) //Transitive
	api(libs.kotlinx.coroutines.core) //Transitive

	debugApi(libs.androidx.compose.runtime) //Transitive
	releaseImplementation(libs.androidx.compose.runtime) //Transitive

	implementation(libs.androidx.activity.compose) //Transitive
	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.core) //Transitive
	implementation(libs.androidx.lifecycle.common) //Transitive
	implementation(libs.androidx.lifecycle.viewmodel) //Transitive
	implementation(libs.androidx.savedState) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensionsJetBrains)
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.kotlin.parcelize.runtime) //Transitive

	runtimeOnly(libs.androidx.compose.ui.tooling)

	debugRuntimeOnly(platform(libs.androidx.compose.bom))
	//Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.compose.ui.testManifest)

	androidTestImplementation(libs.baBeStudios.base.kotlin)
	androidTestImplementation(libs.google.gson)
	androidTestImplementation(platform(libs.google.firebase.bom))
	androidTestImplementation(libs.google.firebase.analytics)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	androidTestImplementation(libs.androidx.test.espresso.core)
	//androidTestImplementation(libs.androidx.test.ext.jUnitKtx) //For ActivityScenario
	androidTestImplementation(libs.androidx.test.runner)
	androidTestImplementation(libs.decompose.core)
	androidTestImplementation(libs.kotlinResult.result)
	androidTestImplementation(libs.test.jUnit)
	androidTestImplementation(libs.bundles.mockk.android)
	androidTestImplementation(libs.androidx.compose.ui.test) //Transitive
	androidTestImplementation(libs.androidx.compose.ui.text) //Transitive
	androidTestImplementation(libs.androidx.test.ext.jUnit) //Transitive from androidx.compose.ui:ui-test-junit4
	androidTestImplementation(libs.test.hamcrest) //Transitive

}